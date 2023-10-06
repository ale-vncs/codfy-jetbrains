package com.ale.vncs.codfy.services

import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.enum.SpotifyStatus
import com.ale.vncs.codfy.notification.Notification
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.utils.Constants
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import se.michaelthelin.spotify.exceptions.detailed.BadGatewayException
import se.michaelthelin.spotify.exceptions.detailed.ForbiddenException
import se.michaelthelin.spotify.exceptions.detailed.ServiceUnavailableException
import java.net.UnknownHostException
import java.util.*

@Service
class SpotifyPlayTrackUpdate {
    private var timer: Timer? = null
    private var tickWaitToUpdate = 0

    fun start() {
        if (timer != null) return
        timer = Timer()
        timer!!.schedule(UpdateTask(), 0, 800)
        println("SpotifyPlayTrackUpdate started")
    }

    fun stop() {
        timer?.cancel()
        timer = null
        println("SpotifyPlayTrackUpdate stopped")
    }

    private class UpdateTask : TimerTask() {
        private val spotifyService = SpotifyService.instance()
        private val notifierService = NotifierService.instance()
        private val spotifyDeviceService = SpotifyDeviceService.instance()
        private val spotifyApi = spotifyService.getApi()

        override fun run() {
            try {
                val data = spotifyApi.informationAboutUsersCurrentPlayback.build().execute()
                val tickWaitToUpdate = getTickWaitToUpdate()
                if (tickWaitToUpdate > 0) {
                    removeTick()
                    return
                }
                if (data != null && data.item != null) {
                    notifierService.setPlayerTracker(PlayerDTO(data))
                } else {
                    spotifyDeviceService.reloadDevices().join()
                    val devices = spotifyDeviceService.getDevices()
                    if (devices.isEmpty()) {
                        stop()
                    }
                }
            } catch (ex: Exception) {
                if (ex is BadGatewayException || ex is ServiceUnavailableException) {
                    return
                }

                if (ex is UnknownHostException) {
                    spotifyService.changeStatus(SpotifyStatus.LOST_CONNECTION)
                    Notification.notifyInfo(
                        "Connection Lost",
                        "Are you connected?"
                    )
                    return
                }
                thisLogger().error(ex)

                cancel()
                spotifyService.logout()
                if (ex is ForbiddenException) {
                    Notification.notifyInfo(
                        "New Login",
                        "You need provide a new authorization to access the new features of ${Constants.APP_NAME}"
                    )
                } else {
                    Notification.notifyError(
                        "Error",
                        "An error occurred! Try login again, Sorry..."
                    )
                }
            }
        }
    }

    companion object {
        private val instance = service<SpotifyPlayTrackUpdate>()

        fun start() {
            instance.start()
        }

        fun stop() {
            instance.stop()
        }

        fun setTickWaitToUpdate(tick: Int) {
            if (instance.tickWaitToUpdate >= tick) return
            instance.tickWaitToUpdate = tick
        }

        fun getTickWaitToUpdate(): Int {
            return instance.tickWaitToUpdate
        }

        fun removeTick() {
            if (instance.tickWaitToUpdate <= 0) {
                instance.tickWaitToUpdate = 0
                return
            }
            instance.tickWaitToUpdate--
        }
    }
}
