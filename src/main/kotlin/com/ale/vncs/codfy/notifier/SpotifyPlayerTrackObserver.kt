package com.ale.vncs.codfy.notifier

import com.ale.vncs.codfy.dto.PlayerDTO

interface SpotifyPlayerTrackObserver {
    fun update(playerData: PlayerDTO)
}
