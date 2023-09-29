package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.component.CustomLabel
import com.ale.vncs.codfy.component.GridBagPanel
import com.ale.vncs.codfy.dto.DeviceDTO
import com.ale.vncs.codfy.utils.Constants
import com.ale.vncs.codfy.component.input.slider.CustomSlider
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackObserver
import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.notifier.SpotifyDeviceChangeObserver
import com.ale.vncs.codfy.services.SpotifySendAction
import com.ale.vncs.codfy.utils.ConvertUtil.convertTime
import com.intellij.util.ui.JBInsets
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JSlider


class SongPeriodBar : GridBagPanel(), SpotifyPlayerTrackObserver, SpotifyDeviceChangeObserver {
    private val labelSongCurrentTime: JLabel
    private val labelSongDurationTime: JLabel
    private val songBar = songDurationBar()

    init {
        labelSongCurrentTime = getLabelTime()
        labelSongDurationTime = getLabelTime()
        createUI()
    }

    override fun createUI() {
        add(labelSongCurrentTime, getTimeStartLabelConstraint())
        add(songBar, progressBarConstraint())
        add(labelSongDurationTime, getTimeEndLabelConstraint())
    }

    private fun songDurationBar(): JSlider {
        val slider = CustomSlider()

        slider.minimum = 0
        slider.maximum = 0
        slider.value = 0
        slider.addMouseListener(object : MouseAdapter() {
            override fun mouseReleased(e: MouseEvent) {
                val value = (e.source as JSlider).value
                labelSongCurrentTime.text = convertTime(value)
                SpotifySendAction.seekToPosition(value)
            }
        })

        return slider
    }

    private fun progressBarConstraint(): GridBagConstraints {
        val constraint = getConstraint()

        constraint.fill = GridBagConstraints.HORIZONTAL
        constraint.weightx = 1.0
        constraint.gridx = 1
        constraint.insets = JBInsets.create(0, 5)

        return constraint
    }

    private fun getLabelTime(): JLabel {
        val label = CustomLabel()

        label.verticalAlignment = Label.CENTER
        label.foreground = Constants.TIME_FONT_COLOR
        label.text = convertTime(0)

        return label
    }

    private fun getTimeStartLabelConstraint(): GridBagConstraints {
        val constraint = getConstraint()

        constraint.gridx = 0
        constraint.anchor = GridBagConstraints.LINE_START

        return constraint
    }

    private fun getTimeEndLabelConstraint(): GridBagConstraints {
        val constraint = getConstraint()

        constraint.gridx = 2
        constraint.anchor = GridBagConstraints.LINE_END

        return constraint
    }

    override fun update(playerData: PlayerDTO) {
        songBar.value = playerData.progressMs
        labelSongCurrentTime.text = convertTime(playerData.progressMs)
        songBar.maximum = playerData.durationMs
        labelSongDurationTime.text = convertTime(playerData.durationMs)
        repaint()
    }

    override fun update(device: DeviceDTO?) {
        songBar.isEnabled = device != null
    }

    override fun addNotify() {
        NotifierService.instance().addSpotifyTrackerObserver(this)
        NotifierService.instance().addSpotifyDeviceChangeObserver(this)
        super.addNotify()
    }

    override fun removeNotify() {
        NotifierService.instance().removeSpotifyTrackerObserver(this)
        NotifierService.instance().removeSpotifyDeviceChangeObserver(this)
        super.removeNotify()
    }
}
