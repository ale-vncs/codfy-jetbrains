package com.ale.vncs.codfy.dto

import se.michaelthelin.spotify.model_objects.specification.PlayHistory
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.SavedTrack
import se.michaelthelin.spotify.model_objects.specification.Track
import java.util.*

class PlaylistTrackData {
    var addedAt: Date
    var isLocal: Boolean
    var track: TrackDTO

    constructor(playlistTrack: PlaylistTrack) {
        this.addedAt = playlistTrack.addedAt
        this.isLocal = playlistTrack.isLocal
        this.track = TrackDTO(playlistTrack.track as Track)
    }

    constructor(playlistTrack: SavedTrack) {
        this.addedAt = playlistTrack.addedAt
        this.track = TrackDTO(playlistTrack.track)
        this.isLocal = false
    }

    constructor(playlistTrack: PlayHistory) {
        this.addedAt = playlistTrack.playedAt
        this.track = TrackDTO(playlistTrack.track as Track)
        this.isLocal = false
    }
}
