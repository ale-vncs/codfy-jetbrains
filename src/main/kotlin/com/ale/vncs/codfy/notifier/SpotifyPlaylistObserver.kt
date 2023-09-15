package com.ale.vncs.codfy.notifier

import com.ale.vncs.codfy.dto.PlaylistDTO

interface SpotifyPlaylistObserver {
    fun update(playlist: PlaylistDTO)
}
