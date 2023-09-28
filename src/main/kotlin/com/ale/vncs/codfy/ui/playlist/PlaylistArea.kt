package com.ale.vncs.codfy.ui.playlist

import com.ale.vncs.codfy.component.DefaultPanel
import com.ale.vncs.codfy.dto.PlaylistDTO
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.services.SpotifyService
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.IconUtil
import com.intellij.util.ui.ImageUtil
import org.imgscalr.Scalr
import org.kordamp.ikonli.materialdesign2.MaterialDesignH
import org.kordamp.ikonli.swing.FontIcon
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.*

private const val i = 8

class PlaylistArea : JBScrollPane() {
    private val notifierService = NotifierService.instance()
    private val spotifyApi = SpotifyService.instance().getApi()
    private val panel = DefaultPanel()

    init {
        border = BorderFactory.createEmptyBorder()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.maximumSize = Dimension(Integer.MAX_VALUE, 100)
        setViewportView(panel)
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)
        songLikedButton()
        spotifyApi.listOfCurrentUsersPlaylists.build().executeAsync().thenApply(fun(playlist) {
            playlist.items.forEach(fun(it) {
                addGap()
                createBoxPlaylist(it)
            })
        })
        getTrackLiked()
    }

    private fun createBoxPlaylist(pl: PlaylistSimplified) {
        var image = ImageIO.read(URI.create(pl.images[0].url).toURL())
        image = Scalr.resize(ImageUtil.toBufferedImage(image!!), 90)
        image = ImageUtil.createRoundedImage(image, 0.0)
        val icon = ImageIcon(image)
        val button = getButton(icon, pl.name)

        button.addActionListener(fun(_) {
            notifierService.setPlaylist(PlaylistDTO.loading())
            spotifyApi.getPlaylistsItems(pl.id).build()
                .executeAsync()
                .thenApply(fun(items) {
                    notifierService.setPlaylist(PlaylistDTO.fromPlaylistTrack(pl, items))
                })
                .exceptionally(fun(_) {
                    notifierService.setPlaylist(PlaylistDTO.error())
                })
        })

        panel.add(button)
    }

    private fun songLikedButton() {
        val icon = FontIcon.of(MaterialDesignH.HEART_CIRCLE, 40, JBColor.PINK)
        val button = getButton(icon, "Liked songs")

        button.addActionListener(fun(_) {
            getTrackLiked()
        })
        button.isRequestFocusEnabled = true
        panel.add(button)
    }

    private fun getTrackLiked() {
        notifierService.setPlaylist(PlaylistDTO.loading())
        spotifyApi.usersSavedTracks.build()
            .executeAsync()
            .thenApply(fun(items) {
                val user = SpotifyService.instance().getUser()
                notifierService.setPlaylist(PlaylistDTO.fromSavedTrack(user.id, items))
            })
            .exceptionally(fun(_) {
                notifierService.setPlaylist(PlaylistDTO.error())
            })
    }

    private fun homeButton() {
        val icon = FontIcon.of(MaterialDesignH.HOME, 40, JBColor.GRAY)
        val button = getButton(icon, "Home")
        button.addActionListener(fun(_) {
            notifierService.setPlaylist(PlaylistDTO.loading())
            spotifyApi.currentUsersRecentlyPlayedTracks.build()
                .executeAsync()
                .thenApply(fun(items) {
                    val user = SpotifyService.instance().getUser()
                    notifierService.setPlaylist(PlaylistDTO.fromPlayHistory(user.id, items))
                })
                .exceptionally(fun(_) {
                    notifierService.setPlaylist(PlaylistDTO.error())
                })
        })

        panel.add(button)
    }


    private fun getButton(icon: Icon, toolTip: String): JButton {
        val iconBorder = 4
        val button = JButton()
        button.icon = icon
        button.preferredSize = Dimension(icon.iconWidth + iconBorder, icon.iconHeight + iconBorder)
        button.toolTipText = toolTip
        button.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

        return button
    }

    private fun addGap() {
        panel.add(Box.createHorizontalStrut(12))
    }
}
