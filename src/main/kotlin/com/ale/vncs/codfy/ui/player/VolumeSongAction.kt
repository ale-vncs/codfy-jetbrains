package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.component.input.icon.button.IconButton
import com.ale.vncs.codfy.component.input.slider.CustomSlider
import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackObserver
import com.ale.vncs.codfy.services.SpotifyService
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.ui.awt.RelativePoint
import org.kordamp.ikonli.materialdesign2.MaterialDesignV
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JSlider
import javax.swing.SwingUtilities

class VolumeSongAction : IconButton(MaterialDesignV.VOLUME_HIGH, 25), SpotifyPlayerTrackObserver {
    private val spotifyApi = SpotifyService.instance().getApi()
    private val slider = CustomSlider()
    private var isPopOpen = false

    init {
        volumeSlider()
        addActionListener(fun(_) {
            volumePopupSlider()
        })
    }

    private fun volumeSlider() {
        slider.maximum = 100
        slider.setSize(200, 25)

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
        popup.addListener(object : JBPopupListener {
            override fun onClosed(event: LightweightWindowEvent) {
                isPopOpen = false
            }

            override fun beforeShown(event: LightweightWindowEvent) {
                isPopOpen = true
            }
        })
        popup.show(rp)
    }

    override fun update(playerData: PlayerDTO) {
        if (isPopOpen) return

        val device = playerData.device
        if (device == null) {
            this.isEnabled = false
        } else {
            this.isEnabled = device.isSupportsVolume
            slider.value = device.volumePercent
        }
    }

    override fun addNotify() {
        slider.value = NotifierService.instance().getDevice()?.volumePercent ?: 0
        NotifierService.instance().addSpotifyTrackerObserver(this)
        super.addNotify()
    }

    override fun removeNotify() {
        NotifierService.instance().removeSpotifyTrackerObserver(this)
        super.removeNotify()
    }
}
