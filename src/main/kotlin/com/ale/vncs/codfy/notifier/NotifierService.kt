package com.ale.vncs.codfy.notifier

import com.ale.vncs.codfy.dto.DeviceDTO
import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.dto.PlaylistDTO
import com.ale.vncs.codfy.enum.SpotifyStatus
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service

@Service
class NotifierService {
    private var spotifyStatus = SpotifyStatus.INITIALIZING
    private var spotifyPlayerTracker: PlayerDTO? = null
    private var spotifyDevice: DeviceDTO? = null

    private val spotifyStatusObserverList: MutableList<SpotifyStatusObserver> = mutableListOf();
    private val spotifyPlayerTrackObserverList: MutableList<SpotifyPlayerTrackObserver> = mutableListOf();
    private val spotifyPlaylistObserverList: MutableList<SpotifyPlaylistObserver> = mutableListOf();
    private val spotifyPlayerTrackChangeObserverList: MutableList<SpotifyPlayerTrackChangeObserver> = mutableListOf();
    private val spotifyDeviceChangeObserverList: MutableList<SpotifyDeviceChangeObserver> = mutableListOf();

    fun addSpotifyTrackerObserver(observer: SpotifyPlayerTrackObserver) {
        this.spotifyPlayerTrackObserverList.add(observer)
    }

    fun removeSpotifyTrackerObserver(observer: SpotifyPlayerTrackObserver) {
        this.spotifyPlayerTrackObserverList.remove(observer)
    }

    fun addSpotifyStatusObserver(observer: SpotifyStatusObserver) {
        this.spotifyStatusObserverList.add(observer)
    }

    fun removeSpotifyStatusObserver(observer: SpotifyStatusObserver) {
        this.spotifyStatusObserverList.remove(observer)
    }

    fun addSpotifyPlaylistObserver(observer: SpotifyPlaylistObserver) {
        this.spotifyPlaylistObserverList.add(observer)
    }

    fun removeSpotifyPlaylistObserver(observer: SpotifyPlaylistObserver) {
        this.spotifyPlaylistObserverList.remove(observer)
    }

    fun addSpotifyPlayerTrackChangeObserver(observer: SpotifyPlayerTrackChangeObserver) {
        this.spotifyPlayerTrackChangeObserverList.add(observer)
    }

    fun removeSpotifyPlayerTrackChangeObserver(observer: SpotifyPlayerTrackChangeObserver) {
        this.spotifyPlayerTrackChangeObserverList.remove(observer)
    }

    fun addSpotifyDeviceChangeObserver(observer: SpotifyDeviceChangeObserver) {
        this.spotifyDeviceChangeObserverList.add(observer)
    }

    fun removeSpotifyDeviceChangeObserver(observer: SpotifyDeviceChangeObserver) {
        this.spotifyDeviceChangeObserverList.remove(observer)
    }

    fun getSpotifyStatus(): SpotifyStatus {
        return spotifyStatus
    }

    fun getPlayerTrack(): PlayerDTO? {
        return spotifyPlayerTracker
    }

    fun getDevice(): DeviceDTO? {
        return this.spotifyDevice
    }

    fun setSpotifyStatus(status: SpotifyStatus) {
        if (status == spotifyStatus) return
        spotifyStatus = status
        for (observer in cloneList(this.spotifyStatusObserverList)) {
            observer.update(status)
        }
    }

    fun setPlayerTracker(playerData: PlayerDTO) {
        setDevice(playerData.device)
        if (spotifyPlayerTracker?.songId != playerData.songId) {
            for (observer in cloneList(this.spotifyPlayerTrackChangeObserverList)) {
                observer.update(playerData)
            }
        }
        spotifyPlayerTracker = playerData
        for (observer in cloneList(this.spotifyPlayerTrackObserverList)) {
            observer.update(playerData)
        }
    }

    fun setPlaylist(playlist: PlaylistDTO) {
        for(observer in cloneList(this.spotifyPlaylistObserverList)) {
            observer.update(playlist)
        }
    }

    fun setDevice(device: DeviceDTO?) {
        if (spotifyDevice?.equals(device) == true) return
        println("device change")
        this.spotifyDevice = device
        for(observer in cloneList(this.spotifyDeviceChangeObserverList)) {
            observer.update(device)
        }
    }

    private fun <T> cloneList(list: MutableList<T>): MutableList<T> {
        val cloneList: MutableList<T> = mutableListOf()
        cloneList.addAll(list)
        return cloneList
    }

    companion object {
        fun instance(): NotifierService {
            return service<NotifierService>()
        }
    }
}
