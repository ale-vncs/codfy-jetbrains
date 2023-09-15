package com.ale.vncs.codfy.dto

import com.ale.vncs.codfy.enum.SpotifyRepeatMode
import se.michaelthelin.spotify.enums.Action
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext
import se.michaelthelin.spotify.model_objects.specification.Track

class PlayerDTO : TrackDTO {
    var progressMs = 0
    var isPlaying = false
    var repeatMode = SpotifyRepeatMode.OFF
    var isShuffle = false
    var actionDisabled: MutableList<Action> = mutableListOf()

    constructor(currentPlaying: CurrentlyPlayingContext) : super(currentPlaying.item as Track) {
        this.progressMs = currentPlaying.progress_ms
        this.isPlaying = currentPlaying.is_playing
        this.isShuffle = currentPlaying.shuffle_state
        this.repeatMode = SpotifyRepeatMode.valueOf(currentPlaying.repeat_state.uppercase())
        this.actionDisabled.addAll(currentPlaying.actions.disallows.disallowedActions.toMutableList())
    }
}
