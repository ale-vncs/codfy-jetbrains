package com.ale.vncs.codfy.dto

import se.michaelthelin.spotify.model_objects.miscellaneous.Device

class DeviceDTO {
    val id: String
    val isActive: Boolean
    val isRestricted: Boolean
    val name: String
    val type: String
    val volumePercent: Int

    constructor(device: Device) {
        this.id = device.id
        this.isActive = device.is_active
        this.isRestricted = device.is_restricted
        this.name = device.name
        this.type = device.type
        this.volumePercent = device.volume_percent
    }
}
