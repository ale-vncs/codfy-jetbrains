package com.ale.vncs.codfy.notifier

import com.ale.vncs.codfy.dto.TrackDTO

interface SpotifyPlayerTrackChangeObserver {
    fun update(track: TrackDTO)
}
