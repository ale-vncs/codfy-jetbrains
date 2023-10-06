package com.ale.vncs.codfy.services

import com.ale.vncs.codfy.dto.DeviceDTO
import com.ale.vncs.codfy.notification.Notification
import com.ale.vncs.codfy.notifier.NotifierService
import com.google.gson.JsonParser
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import se.michaelthelin.spotify.model_objects.miscellaneous.Device
import java.util.concurrent.CompletableFuture

@Service
class SpotifyDeviceService {
    private val spotifyApi = SpotifyService.instance().getApi()
    private val currentDevice get() = NotifierService.instance().getDevice()

    private var devices: Array<Device> = emptyArray()

    fun getDevices(): Array<DeviceDTO> {
        return devices.map(fun(d): DeviceDTO { return DeviceDTO(d)}).toTypedArray()
    }

    fun getDeviceSelected(): DeviceDTO? {
        val device = this.devices.find(fun(d): Boolean { return d.is_active })
        if (device != null) return DeviceDTO(device)
        SpotifyPlayTrackUpdate.stop()
        return null
    }

    fun changeDevice(device: DeviceDTO) {
        if (device == currentDevice) return

        val ids = JsonParser.parseString("[\"${device.id}\"]").asJsonArray
        spotifyApi.transferUsersPlayback(ids)
            .play(true)
            .build()
            .executeAsync()
            .thenApply(fun(_) {
                SpotifyPlayTrackUpdate.start()
            })
            .exceptionally(fun(ex) {
                Notification.notifyError("Change device", "An error occurred when change to ${device.name}")
                thisLogger().info("Error when change device")
                thisLogger().error(ex)
                getDevices()
            })
    }

    fun reloadDevices(): CompletableFuture<Unit> {
        return spotifyApi.usersAvailableDevices.build()
            .executeAsync()
            .thenApply(fun(devices) {
                this.devices = devices
                if (devices.isEmpty()) {
                    thisLogger().info("No device selected")
                    Notification.notifyInfo(
                        "No Device",
                        "You need a device to play spotify"
                    )
                }
            }).exceptionally(fun(ex) {
                thisLogger().error("An error occurred a get available devices")
                thisLogger().error(ex)
            })
    }

    companion object {
        fun instance(): SpotifyDeviceService {
            return service<SpotifyDeviceService>()
        }
    }
}
