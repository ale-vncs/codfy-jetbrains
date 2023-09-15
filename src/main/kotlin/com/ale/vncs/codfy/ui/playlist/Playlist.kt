package com.ale.vncs.codfy.ui.playlist

import com.ale.vncs.codfy.component.DefaultPanel
import com.ale.vncs.codfy.dto.PlaylistDTO
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlaylistObserver
import com.intellij.util.ui.JBFont
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.SwingConstants

class Playlist : DefaultPanel(BorderLayout()), SpotifyPlaylistObserver {
    private var playlist: PlaylistDTO? = null
    private val label = JLabel(playlist?.name ?: "")
    private val centerPanel = DefaultPanel(BorderLayout(0  ,4))

    init {
        NotifierService.instance().addSpotifyPlaylistObserver(this)
        add(PlaylistArea(), BorderLayout.NORTH)
        playlistName()
        centerPanel.add(PlaylistSong())
        add(centerPanel)
    }

    private fun playlistName() {
        label.horizontalTextPosition = SwingConstants.CENTER
        label.horizontalAlignment = SwingConstants.CENTER
        label.font = JBFont.regular().asBold().deriveFont(20f)
        centerPanel.add(label, BorderLayout.NORTH)
    }

    override fun update(playlist: PlaylistDTO) {
        this.playlist = playlist
        label.text = playlist.name
        repaint()
    }
}
