package com.ale.vncs.codfy.services

import java.time.LocalDateTime
import java.util.*

object SpotifyRefreshTokenService {
    private var timer: Timer? = null

    fun start(expiresIn: Int = 0) {
        if (timer != null) return
        timer = Timer()
        val currentDate = LocalDateTime.now().plusSeconds((expiresIn / 2).toLong())
        println("Scheduler refresh token task to: $currentDate")
        timer?.schedule(RefreshTokenTask(), ((expiresIn / 2) * 1000).toLong())
    }

    fun stop() {
        timer?.cancel()
    }

    private class RefreshTokenTask : TimerTask() {
        private val spotifyService = SpotifyService.instance()
        override fun run() {
            val credential = spotifyService.refreshToken()
            start(credential?.expiresIn ?: 10)
        }
    }
}
