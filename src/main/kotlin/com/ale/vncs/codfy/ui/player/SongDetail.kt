package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.component.CustomLabel
import com.ale.vncs.codfy.component.GridBagPanel
import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackObserver
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints


class SongDetail : GridBagPanel(), SpotifyPlayerTrackObserver {
    private var playerData: PlayerDTO?
    private var songName = CustomLabel()
    private var songImage = CustomLabel()
    private var artistName = CustomLabel()
    private val notifierService = NotifierService.instance()

    init {
        playerData = notifierService.getPlayerTrack()
        createUI()
    }

    override fun createUI() {
        songImage()
        songName()
        songArtist()
    }

    private fun songImage() {
        val constraint = getConstraint()

        constraint.gridheight = 3

        add(songImage, constraint)
    }

    private fun songName() {
        val constraint = labelConstraint()

        songName.font = JBFont.regular().deriveFont(20f)
        add(songName, constraint)
    }

    private fun songArtist() {
        val constraint = labelConstraint()

        constraint.gridy = 1

        artistName.font = JBFont.regular().deriveFont(15f)

        add(artistName, constraint)
    }

    private fun labelConstraint(): GridBagConstraints {
        val constraint = getConstraint()

        constraint.gridx = 1
        constraint.weightx = 1.0
        constraint.insets = JBUI.insets(0, 8, 4, 0)
        constraint.anchor = GridBagConstraints.PAGE_START
        constraint.fill = GridBagConstraints.HORIZONTAL

        return constraint
    }

    override fun update(playerData: PlayerDTO) {
        this.playerData = playerData
        songName.text = playerData.songName
        songImage.icon = playerData.songImage
        artistName.text = playerData.songArtistName
        repaint()
    }

    override fun addNotify() {
        notifierService.addSpotifyTrackerObserver(this)
        super.addNotify()
    }

    override fun removeNotify() {
        notifierService.removeSpotifyTrackerObserver(this)
        super.removeNotify()
    }
}
