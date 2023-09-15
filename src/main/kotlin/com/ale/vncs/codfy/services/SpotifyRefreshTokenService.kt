package com.ale.vncs.codfy.services

import com.ale.vncs.codfy.utils.SpotifyTokens
import com.intellij.openapi.diagnostic.thisLogger
import java.time.LocalDateTime
import java.util.*

object SpotifyRefreshTokenService {
    fun start(expiresIn: Int = 0) {
        val currentDate = LocalDateTime.now().plusSeconds((expiresIn / 2).toLong())
        println("Scheduler refresh token task to: $currentDate")
        Timer().schedule(RefreshTokenTask(), ((expiresIn / 2) * 1000).toLong())
    }

    private class RefreshTokenTask : TimerTask() {
        private val spotifyApi = SpotifyService.instance().getApi()
        override fun run() {
            println("Executing Refresh token")
            spotifyApi.authorizationCodeRefresh().build()
                .executeAsync()
                .thenApply(fun(credential) {
                    spotifyApi.accessToken = credential.accessToken
                    SpotifyTokens.setTokens(spotifyApi.accessToken, spotifyApi.refreshToken)
                    start(credential.expiresIn)
                }).exceptionally(fun(ex) {
                    start(10)
                    thisLogger().info("An error occurred when refresh token")
                    thisLogger().error(ex)
                })
        }
    }
}
