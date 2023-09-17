package com.ale.vncs.codfy.dto

import se.michaelthelin.spotify.model_objects.specification.*

class PlaylistDTO {
    var isError = false
    var isLoading = false
    var playlistId = ""
    var name = ""
    var total = 0
    var hasNext = false
    var tracks: List<PlaylistTrackData> = emptyList()
    var uri = ""

    companion object {
        fun fromPlaylistTrack(pl: PlaylistSimplified, playlist: Paging<PlaylistTrack>): PlaylistDTO {
            val playlistDTO = PlaylistDTO()
            playlistDTO.playlistId = pl.id
            playlistDTO.name = pl.name
            playlistDTO.uri = "spotify:playlist:${pl.id}"
            playlistDTO.total = playlist.total
            playlistDTO.hasNext = playlist.next != null
            playlistDTO.tracks = playlist.items
                .map(fun(it): PlaylistTrackData { return PlaylistTrackData(it) })
                .toList()
            return playlistDTO
        }

        fun fromSavedTrack(userId: String, playlist: Paging<SavedTrack>): PlaylistDTO {
            val playlistDTO = PlaylistDTO()
            playlistDTO.total = playlist.total
            playlistDTO.hasNext = playlist.next != null
            playlistDTO.name = "Liked songs"
            playlistDTO.uri = "spotify:user:${userId}:collection"
            playlistDTO.tracks = playlist.items
                .map(fun(it): PlaylistTrackData { return PlaylistTrackData(it) })
                .toList()
            return playlistDTO
        }

        fun fromPlayHistory(userId: String, playlist: PagingCursorbased<PlayHistory>): PlaylistDTO {
            val playlistDTO = PlaylistDTO()
            playlistDTO.total = playlist.total
            playlistDTO.hasNext = playlist.next != null
            playlistDTO.uri = "spotify:user:${userId}:collection"
            playlistDTO.tracks = playlist.items
                .map(fun(it): PlaylistTrackData { return PlaylistTrackData(it) })
                .toList()
            return playlistDTO
        }

        fun loading(): PlaylistDTO {
            val playlistDTO = PlaylistDTO()
            playlistDTO.isLoading = true
            return playlistDTO
        }

        fun error(): PlaylistDTO {
            val playlistDTO = PlaylistDTO()
            playlistDTO.isError = true
            return playlistDTO
        }
    }
}
