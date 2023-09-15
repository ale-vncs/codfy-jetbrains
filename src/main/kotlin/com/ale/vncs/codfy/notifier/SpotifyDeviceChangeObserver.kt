package com.ale.vncs.codfy.notifier

import com.ale.vncs.codfy.dto.DeviceDTO

interface SpotifyDeviceChangeObserver {
    fun update(device: DeviceDTO?)
}
