package com.ale.vncs.codfy.services

import com.ale.vncs.codfy.enum.SpotifyStatus
import com.ale.vncs.codfy.notification.Notification
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.utils.AuthCallback
import com.ale.vncs.codfy.utils.Secret
import com.ale.vncs.codfy.utils.SpotifyTokens
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.SpotifyHttpManager
import se.michaelthelin.spotify.enums.AuthorizationScope
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException
import se.michaelthelin.spotify.model_objects.specification.User
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import java.net.URI
import java.util.concurrent.CancellationException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException


@Service
class SpotifyService {
    private val spotifyApi: SpotifyApi
    private var status: SpotifyStatus = SpotifyStatus.INITIALIZING
    private var user: User? = null
    private val redirectCallbackPort = 56124
    private val CLIENT_ID = Secret.SPOTIFY_CLIENT_ID
    private val CLIENT_SECRET = Secret.SPOTIFY_CLIENT_SECRET

    init {
        spotifyApi = createConnection()
    }

    fun start() {
        changeStatus(SpotifyStatus.INITIALIZING)
        checkAccess()
    }

    fun getUser(): User {
        if (this.user != null) return this.user!!
        throw Exception("no user logged")
    }

    fun getApi(): SpotifyApi {
        return spotifyApi
    }

    private fun checkAccess() {
        val (accessKey, refreshToken) = SpotifyTokens.getTokens()
        if (accessKey == null || refreshToken == null) {
            SpotifyTokens.removeTokens()
            changeStatus(SpotifyStatus.NOT_LOGGED)
            Notification.notifyInfo("need login", "You need to connect in spotify")
            return
        }
        spotifyApi.accessToken = accessKey
        spotifyApi.refreshToken = refreshToken
        SpotifyRefreshTokenService.start()
        getUserData()
    }

    private fun getUserData() {
        try {
            val user = spotifyApi.currentUsersProfile.build().execute()

            this.user = user
            changeStatus(SpotifyStatus.LOGGED)
        } catch (ex: Exception) {
            thisLogger().error(ex)
            if (ex is UnauthorizedException) {
                Notification.notifyError(
                    "Login Error",
                    "Your access has been expired. Login again!"
                )
            } else {
                Notification.notifyError(
                    "Login Error",
                    "Unfortunately, an error occurred when access your account. Try login again"

                )
            }
            changeStatus(SpotifyStatus.NOT_LOGGED)
            SpotifyTokens.removeTokens()
        }
    }

    fun changeStatus(status: SpotifyStatus) {
        this.status = status
        val stopTrackUpdateByStatus: List<SpotifyStatus> =
            listOf(SpotifyStatus.LOST_CONNECTION, SpotifyStatus.NOT_LOGGED)
        if (stopTrackUpdateByStatus.contains(status)) {
            SpotifyPlayTrackUpdate.stop()
        }
        if (status == SpotifyStatus.LOGGED) {
            SpotifyPlayTrackUpdate.start()
        }
        NotifierService.instance().setSpotifyStatus(status)
    }

    fun login() {
        try {
            AuthCallback.startListener(redirectCallbackPort, this::authorizationCode)
            createAuthorizationRequest().executeAsync().thenApply(fun(uri) {
                println("URI: $uri")
                BrowserUtil.browse(uri)
            })
        } catch (e: CompletionException) {
            println("Error: " + e.cause!!.message)
        } catch (e: CancellationException) {
            println("Async operation cancelled.")
        }
    }

    fun logout() {
        SpotifyTokens.removeTokens()
        spotifyApi.accessToken = null
        spotifyApi.refreshToken = null
        changeStatus(SpotifyStatus.NOT_LOGGED)
    }

    private fun authorizationCode(code: String) {
        try {
            createAuthorizationCode(code)
                .executeAsync()
                .thenApply(fun(credentials) {
                    spotifyApi.accessToken = credentials.accessToken
                    spotifyApi.refreshToken = credentials.refreshToken
                    SpotifyRefreshTokenService.start(credentials.expiresIn)
                    saveTokens()
                    getUserData()
                    Notification.notifyInfo("Login Successfully", "Thanks for using us!!")
                })
        } catch (e: CompletionException) {
            println("Error: " + e.cause!!.message)
        } catch (e: CancellationException) {
            println("Async operation cancelled.")
        }
    }

    private fun createAuthorizationRequest(): AuthorizationCodeUriRequest {
        return spotifyApi
            .authorizationCodeUri()
            .show_dialog(false)
            .response_type("code")
            .scope(
                AuthorizationScope.USER_LIBRARY_READ,
                AuthorizationScope.USER_LIBRARY_MODIFY,
                AuthorizationScope.USER_READ_CURRENTLY_PLAYING,
                AuthorizationScope.USER_MODIFY_PLAYBACK_STATE,
                AuthorizationScope.USER_READ_PLAYBACK_STATE,
                AuthorizationScope.USER_READ_PRIVATE
            )
            .build()
    }

    private fun createAuthorizationCode(code: String): AuthorizationCodeRequest {
        return spotifyApi
            .authorizationCode(code)
            .build()
    }

    private fun createConnection(): SpotifyApi {
        return SpotifyApi.builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(CLIENT_SECRET)
            .setRedirectUri(buildRedirectUri(redirectCallbackPort))
            .build()
    }

    private fun buildRedirectUri(port: Int): URI {
        return SpotifyHttpManager.makeUri("http://localhost:$port/callback/jetbrains")
    }

    private fun saveTokens() {
        SpotifyTokens.setTokens(spotifyApi.accessToken, spotifyApi.refreshToken)
    }

    companion object {
        fun instance(): SpotifyService {
            return service<SpotifyService>()
        }
    }
}
