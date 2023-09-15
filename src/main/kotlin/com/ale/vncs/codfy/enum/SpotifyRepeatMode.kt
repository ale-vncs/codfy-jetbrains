package com.ale.vncs.codfy.enum

enum class SpotifyRepeatMode {
    OFF,
    TRACK,
    CONTEXT;

    fun nextMode(): String {
        return when(this) {
            OFF -> "context"
            CONTEXT -> "track"
            TRACK -> "off"
        }
    }
}
