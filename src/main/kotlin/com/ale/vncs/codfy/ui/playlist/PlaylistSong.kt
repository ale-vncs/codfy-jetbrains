package com.ale.vncs.codfy.ui.playlist

import AlphaContainer
import com.ale.vncs.codfy.component.DefaultPanel
import com.ale.vncs.codfy.dto.PlaylistDTO
import com.ale.vncs.codfy.dto.PlaylistTrackData
import com.ale.vncs.codfy.dto.TrackDTO
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackChangeObserver
import com.ale.vncs.codfy.notifier.SpotifyPlaylistObserver
import com.ale.vncs.codfy.services.SpotifyService
import com.ale.vncs.codfy.utils.Constants
import com.ale.vncs.codfy.utils.ConvertUtil.convertTime
import com.google.gson.JsonParser
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.IconUtil.toBufferedImage
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBInsets
import org.imgscalr.Scalr
import org.kordamp.ikonli.materialdesign2.MaterialDesignP
import org.kordamp.ikonli.swing.FontIcon
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class PlaylistSong : JBScrollPane(), SpotifyPlaylistObserver, SpotifyPlayerTrackChangeObserver {
    private val notifierService = NotifierService.instance()
    private var playlist: PlaylistDTO = PlaylistDTO()
    private var trackData: TrackDTO? = null
    private val panel = DefaultPanel()

    init {
        border = BorderFactory.createEmptyBorder()
        trackData = notifierService.getPlayerTrack()
        notifierService.addSpotifyPlaylistObserver(this)
        notifierService.addSpotifyPlayerTrackChangeObserver(this)

        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        setViewportView(AlphaContainer(panel))
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
    }

    private fun trackCardLine(playlistTrackData: PlaylistTrackData) {
        val card = DefaultPanel(GridBagLayout())

        val image = Scalr.resize(toBufferedImage(playlistTrackData.track.songImage), 60)
        val constraint = GridBagConstraints()

        constraint.insets = JBInsets.create(0, 8)
        constraint.anchor = GridBagConstraints.LINE_START

        val iconLabel = JLabel()
        iconLabel.preferredSize = Dimension(30, 30)
        if (trackData?.songId == playlistTrackData.track.songId) {
            iconLabel.icon = FontIcon.of(MaterialDesignP.PLAY_CIRCLE, 30, Constants.SPOTIFY_COLOR)
        }
        card.add(iconLabel)
        constraint.gridx = constraint.gridx++

        card.add(JLabel(ImageIcon(image)), constraint)

        constraint.gridx = constraint.gridx++
        constraint.fill = GridBagConstraints.HORIZONTAL
        constraint.weightx = 1.0

        card.add(songDescriptionPanel(playlistTrackData.track), constraint)

        constraint.gridx = constraint.gridx++
        constraint.fill = GridBagConstraints.NONE
        constraint.weightx = 0.0

        card.add(getLabel(convertTime(playlistTrackData.track.durationMs)))

        card.addMouseListener(PanelMouseListener(playlistTrackData.track, playlist))

        panel.add(card)
        addGap()
    }

    private fun updateUi() {
        panel.removeAll()
        panel.repaint()
        if (playlist.isLoading) {
            loadingPanel()
        } else {
            addGap()
            playlist.tracks.forEach(this::trackCardLine)
        }
    }

    private fun songDescriptionPanel(track: TrackDTO): JPanel {
        val panel = JPanel()
        panel.isOpaque = false
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(getLabel(track.songName, 18f))
        panel.add(Box.createVerticalGlue())
        panel.add(getLabel(track.songArtistName))

        return panel
    }

    private fun getLabel(text: String, size: Float = 12f): JLabel {
        val label = JLabel(text)

        label.horizontalAlignment = SwingConstants.LEFT
        label.font = JBFont.regular().deriveFont(size)

        return label
    }

    private fun loadingPanel() {
        val panel = DefaultPanel(GridBagLayout())
        panel.add(JLabel("Loading..."), GridBagConstraints())
        this.panel.add(panel)
    }

    internal class PanelMouseListener(val track: TrackDTO, val playlist: PlaylistDTO) : MouseAdapter() {
        private val spotifyApi = SpotifyService.instance().getApi()
        override fun mouseEntered(e: MouseEvent) {
            val panel = e.source as JPanel
            panel.isOpaque = true
            panel.background = JBColor.WHITE.brighter()
            panel.repaint()
        }

        override fun mouseClicked(e: MouseEvent?) {
            spotifyApi.startResumeUsersPlayback()
                .context_uri(playlist.uri)
                .offset(JsonParser.parseString("{\"uri\":\"${track.uri}\"}").asJsonObject)
                .build()
                .executeAsync()
        }

        override fun mouseExited(e: MouseEvent) {
            val panel = e.source as JPanel
            panel.isOpaque = false
            panel.background = null
            panel.repaint()
        }
    }

    private fun addGap() {
        panel.add(Box.createVerticalStrut(8))
    }

    override fun update(playlist: PlaylistDTO) {
        this.playlist = playlist
        updateUi()
    }

    override fun update(track: TrackDTO) {
        this.trackData = track
        updateUi()
    }
}
