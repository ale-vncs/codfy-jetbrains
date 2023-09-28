package com.ale.vncs.codfy.dto

import se.michaelthelin.spotify.model_objects.miscellaneous.Device

class DeviceDTO {
    val id: String
    val isActive: Boolean
    val isRestricted: Boolean
    val name: String
    val type: String
    val volumePercent: Int
    val isSupportsVolume: Boolean

    constructor(device: Device) {
        this.id = device.id
        this.isActive = device.is_active
        this.isRestricted = device.is_restricted
        this.name = device.name
        this.type = device.type
        this.volumePercent = device.volume_percent
        this.isSupportsVolume = device.supports_volume
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DeviceDTO) return false

        val device = other as DeviceDTO?
        val equalList: ArrayList<Boolean> = ArrayList()

        equalList.add(device?.id == this.id)
        equalList.add(device?.isActive == this.isActive)
        equalList.add(device?.isRestricted == this.isRestricted)
        equalList.add(device?.name == this.name)
        equalList.add(device?.type == this.type)
        equalList.add(device?.volumePercent == this.volumePercent)
        equalList.add(device?.isSupportsVolume == this.isSupportsVolume)

        return equalList.stream().allMatch(fun(f): Boolean { return f })
    }
}
