package com.ale.vncs.codfy.notifier

import com.ale.vncs.codfy.enum.SpotifyStatus

interface SpotifyStatusObserver {
    fun update(status: SpotifyStatus)
}
