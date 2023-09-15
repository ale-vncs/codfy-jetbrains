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
        removeIfExist(this.spotifyPlayerTrackObserverList, observer)
        this.spotifyPlayerTrackObserverList.add(observer)
    }

    fun addSpotifyStatusObserver(observer: SpotifyStatusObserver) {
        removeIfExist(this.spotifyStatusObserverList, observer)
        this.spotifyStatusObserverList.add(observer)
    }

    fun addSpotifyPlaylistObserver(observer: SpotifyPlaylistObserver) {
        removeIfExist(this.spotifyPlaylistObserverList, observer)
        this.spotifyPlaylistObserverList.add(observer)
    }

    fun addSpotifyPlayerTrackChangeObserver(observer: SpotifyPlayerTrackChangeObserver) {
        removeIfExist(this.spotifyPlayerTrackChangeObserverList, observer)
        this.spotifyPlayerTrackChangeObserverList.add(observer)
    }

    fun addSpotifyDeviceChangeObserver(observer: SpotifyDeviceChangeObserver) {
        removeIfExist(this.spotifyDeviceChangeObserverList, observer)
        this.spotifyDeviceChangeObserverList.add(observer)
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

    private fun <T> removeIfExist(list: MutableList<T>, clazz: T) {
        val className = (clazz as Any).javaClass.name
        list.removeIf(fun(it): Boolean { return (it as Any).javaClass.name == className })
    }

    companion object {
        fun instance(): NotifierService {
            return service<NotifierService>()
        }
    }
}
