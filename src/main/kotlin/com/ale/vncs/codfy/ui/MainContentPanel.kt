package com.ale.vncs.codfy.ui

import com.ale.vncs.codfy.component.DefaultPanel
import com.ale.vncs.codfy.enum.SpotifyStatus
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyStatusObserver
import com.ale.vncs.codfy.ui.auth.AuthPanel
import com.ale.vncs.codfy.ui.player.PlayerPanel
import com.ale.vncs.codfy.ui.playlist.Playlist
import com.intellij.openapi.wm.ToolWindow
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class MainContentPanel(val toolWindow: ToolWindow) : DefaultPanel(BorderLayout(0, 8)), SpotifyStatusObserver {
    private val notifierService = NotifierService.instance()
    private var spotifyStatus: SpotifyStatus

    init {
        spotifyStatus = notifierService.getSpotifyStatus()
        notifierService.addSpotifyStatusObserver(this)
        val padding = 8
        border = BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        createUI()
    }

    private fun createUI() {
        removeAll()
        repaint()
        add(Header(), BorderLayout.NORTH)
        if (spotifyStatus == SpotifyStatus.NOT_LOGGED) {
            add(AuthPanel())
        }
        if (spotifyStatus == SpotifyStatus.INITIALIZING) {
            add(initializingPanel())
        }
        if (spotifyStatus == SpotifyStatus.LOGGED) {
            add(Playlist())
            add(PlayerPanel(), BorderLayout.SOUTH)
        }
        println("Main content updated")
    }

    private fun initializingPanel(): JPanel {
        val panel = JPanel()

        panel.add(JLabel("Initializing...", SwingConstants.CENTER))

        return panel
    }

    override fun update(status: SpotifyStatus) {
        spotifyStatus = status
        println("status: $status")
        createUI()
    }
}
