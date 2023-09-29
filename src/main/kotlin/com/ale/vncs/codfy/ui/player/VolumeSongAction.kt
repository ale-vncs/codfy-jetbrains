package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.dto.DeviceDTO
import com.ale.vncs.codfy.component.input.icon.button.IconButton
import com.ale.vncs.codfy.component.input.slider.CustomSlider
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyDeviceChangeObserver
import com.ale.vncs.codfy.services.SpotifyService
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import org.kordamp.ikonli.materialdesign2.MaterialDesignV
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JSlider
import javax.swing.SwingUtilities

class VolumeSongAction : IconButton(MaterialDesignV.VOLUME_HIGH, 25), SpotifyDeviceChangeObserver {
    private val spotifyApi = SpotifyService.instance().getApi()
    private val slider = CustomSlider()

    init {
        volumeSlider()
        addActionListener(fun(_) {
            volumePopupSlider()
        })
    }

    private fun volumeSlider() {
        slider.maximum = 100
        slider.setSize(200, 20)

        slider.addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent) {
                spotifyApi.setVolumeForUsersPlayback((e.source as JSlider).value)
                    .build()
                    .executeAsync()
            }
        })
    }

    private fun volumePopupSlider() {
        val popup =
            JBPopupFactory.getInstance().createComponentPopupBuilder(slider, slider)
                .setRequestFocus(true)
                .setCancelOnClickOutside(true)
                .createPopup()
        val point = Point(-(slider.width - this.width), -this.height)
        val rp = RelativePoint(SwingUtilities.convertPoint(this, point, slider))
        popup.show(rp)
    }

    override fun update(device: DeviceDTO?) {
        if (device == null) {
            this.isEnabled = false
        } else {
            this.isEnabled = device.isSupportsVolume
            slider.value = device.volumePercent
        }
    }

    override fun addNotify() {
        slider.value = NotifierService.instance().getDevice()?.volumePercent ?: 0
        NotifierService.instance().addSpotifyDeviceChangeObserver(this)
        super.addNotify()
    }

    override fun removeNotify() {
        NotifierService.instance().removeSpotifyDeviceChangeObserver(this)
        super.removeNotify()
    }
}
