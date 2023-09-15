package com.ale.vncs.codfy.services

import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.notifier.NotifierService
import java.util.concurrent.CompletableFuture

object SpotifySendAction {
    private val spotifyApi = SpotifyService.instance().getApi()
    private val playerData: PlayerDTO? get() = NotifierService.instance().getPlayerTrack()

    fun playPause() {
        if (playerData == null) return
        val isPlaying = playerData!!.isPlaying
        if (!isPlaying) {
            spotifyApi.startResumeUsersPlayback().build().executeAsync()
        } else {
            spotifyApi.pauseUsersPlayback().build().executeAsync()
        }
    }

    fun nextSong() {
        if (playerData == null) return
        spotifyApi.skipUsersPlaybackToNextTrack().build().executeAsync()
    }

    fun prevSong() {
        if (playerData == null) return
        if ((playerData!!.progressMs / 1000) < 3) {
            spotifyApi.skipUsersPlaybackToPreviousTrack().build().executeAsync()
        } else {
            seekToPosition(0)
        }
    }

    fun shuffleSong() {
        if (playerData == null) return
        spotifyApi.toggleShuffleForUsersPlayback(!playerData!!.isShuffle).build().executeAsync()
    }

    fun repeatMode() {
        if (playerData == null) return
        spotifyApi.setRepeatModeOnUsersPlayback(playerData!!.repeatMode.nextMode()).build().executeAsync()
    }

    fun seekToPosition(value: Int) {
        if (playerData == null) return
        spotifyApi.seekToPositionInCurrentlyPlayingTrack(value).build().executeAsync()
    }

    fun toggleLikeTrack(isLiked: Boolean): CompletableFuture<String>? {
        if (playerData == null) return CompletableFuture()
        if(isLiked) {
            return spotifyApi.removeUsersSavedTracks(playerData?.songId).build().executeAsync()
        } else {
            return spotifyApi.saveTracksForUser(playerData?.songId).build().executeAsync()
        }
    }
}
