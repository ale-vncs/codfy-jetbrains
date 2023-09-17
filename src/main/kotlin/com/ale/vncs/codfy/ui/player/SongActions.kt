package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.component.GridBagPanel
import com.ale.vncs.codfy.dto.DeviceDTO
import com.ale.vncs.codfy.utils.Constants
import com.ale.vncs.codfy.enum.SpotifyRepeatMode
import com.ale.vncs.codfy.component.input.icon.button.IconButton
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackObserver
import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.dto.TrackDTO
import com.ale.vncs.codfy.notifier.SpotifyDeviceChangeObserver
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackChangeObserver
import com.ale.vncs.codfy.services.SpotifySendAction
import com.ale.vncs.codfy.services.SpotifyService
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.materialdesign2.MaterialDesignH
import org.kordamp.ikonli.materialdesign2.MaterialDesignP
import org.kordamp.ikonli.materialdesign2.MaterialDesignR
import org.kordamp.ikonli.materialdesign2.MaterialDesignS
import org.kordamp.ikonli.swing.FontIcon
import se.michaelthelin.spotify.enums.Action
import java.awt.*
import javax.swing.JButton
import javax.swing.JPanel


class SongActions : GridBagPanel(), SpotifyPlayerTrackObserver, SpotifyPlayerTrackChangeObserver, SpotifyDeviceChangeObserver {
    private val flowLayout = FlowLayout()
    private val centerPanel = JPanel()
    private var playerData: PlayerDTO? = null
    private var isLikedTrack = false
    private val spotifyApi = SpotifyService.instance().getApi()

    private val playPauseIconSize = 40
    private val playPauseIconButton = getButton(MaterialDesignP.PLAY_CIRCLE, playPauseIconSize)
    private val shuffleIconButton = getButton(MaterialDesignS.SHUFFLE_VARIANT)
    private val repeatIconButton = getButton(MaterialDesignR.REPEAT_OFF)
    private val likeIconButton = getButton(MaterialDesignH.HEART_OUTLINE)
    private val stepForward = getButton(MaterialDesignS.STEP_FORWARD)
    private val stepBackward = getButton(MaterialDesignS.STEP_BACKWARD)

    init {
        NotifierService.instance().addSpotifyTrackerObserver(this)
        NotifierService.instance().addSpotifyPlayerTrackChangeObserver(this)
        NotifierService.instance().addSpotifyDeviceChangeObserver(this)
        flowLayout.hgap = 15
        flowLayout.alignment = FlowLayout.CENTER
        centerPanel.isOpaque = false
        centerPanel.layout = flowLayout
        createUI()
    }

    override fun createUI() {
        shuffleSong()
        backwardSong()
        playPauseIcon()
        forwardSong()
        repeatSong()

        volumeSong()
        likeSong()

        val constraint = getConstraint()

        constraint.gridx = 1
        constraint.weighty = 2.0
        constraint.fill = GridBagConstraints.HORIZONTAL

        add(centerPanel, constraint)
    }

    private fun playPauseIcon() {
        playPauseIconButton.addActionListener { _ ->
            SpotifySendAction.playPause()
        }
        centerPanel.add(playPauseIconButton)
    }

    private fun backwardSong() {
        stepBackward.addActionListener(fun(_) { SpotifySendAction.prevSong() })
        centerPanel.add(stepBackward)
    }

    private fun forwardSong() {
        stepForward.addActionListener(fun(_) { SpotifySendAction.nextSong() })
        centerPanel.add(stepForward)
    }

    private fun shuffleSong() {
        shuffleIconButton.addActionListener(fun(_) { SpotifySendAction.shuffleSong() })
        centerPanel.add(shuffleIconButton)
    }

    private fun repeatSong() {
        repeatIconButton.addActionListener(fun(_) { SpotifySendAction.repeatMode() })
        centerPanel.add(repeatIconButton)
    }

    private fun likeSong() {
        val constraint = getConstraint()

        constraint.gridx = 0
        constraint.anchor = GridBagConstraints.LINE_START
        constraint.weightx = 0.5

        likeIconButton.addActionListener(fun(_) {
            SpotifySendAction.toggleLikeTrack(isLikedTrack)
                ?.thenApply(fun(_) {
                    updateLiked(!isLikedTrack)
                })
        })

        add(likeIconButton, constraint)
    }

    private fun volumeSong() {
        val button = VolumeSongAction()
        val constraint = getConstraint()

        constraint.gridx = 2
        constraint.anchor = GridBagConstraints.LINE_END
        constraint.weightx = 0.5
        add(button, constraint)
    }

    private fun getButton(icon: Ikon, size: Int = 25): JButton {
        return IconButton(icon, size)
    }

    private fun updatePlayPause(playerData: PlayerDTO) {
        val icon = if (playerData.isPlaying) MaterialDesignP.PAUSE_CIRCLE else MaterialDesignP.PLAY_CIRCLE
        changeIcon(playPauseIconButton, icon, playPauseIconSize)
    }

    private fun updateRepeatMode(playerData: PlayerDTO) {
        val repeatMode = playerData.repeatMode
        val icon = when (repeatMode) {
            SpotifyRepeatMode.OFF -> MaterialDesignR.REPEAT_OFF
            SpotifyRepeatMode.CONTEXT -> MaterialDesignR.REPEAT
            SpotifyRepeatMode.TRACK -> MaterialDesignR.REPEAT_ONCE
        }
        val color = if (repeatMode == SpotifyRepeatMode.OFF) Constants.ICON_COLOR else Constants.SPOTIFY_COLOR
        changeIcon(repeatIconButton, icon, color = color)

        val isContainToggleRepeatTrack = playerData.actionDisabled.contains(Action.TOGGLING_REPEAT_TRACK)
        val isContainToggleRepeatContext = playerData.actionDisabled.contains(Action.TOGGLING_REPEAT_CONTEXT)
        repeatIconButton.isEnabled = !isContainToggleRepeatTrack.or(isContainToggleRepeatContext)
    }

    private fun updateShuffle(playerData: PlayerDTO) {
        val color = if (playerData.isShuffle) Constants.SPOTIFY_COLOR else Constants.ICON_COLOR
        changeIcon(shuffleIconButton, MaterialDesignS.SHUFFLE_VARIANT, color = color)
        shuffleIconButton.isEnabled = !playerData.actionDisabled.contains(Action.TOGGLING_SHUFFLE)
    }

    private fun updateLiked(isLiked: Boolean) {
        isLikedTrack = isLiked
        var icon = MaterialDesignH.HEART_OUTLINE
        var color = Constants.ICON_COLOR
        if (isLiked) {
            icon = MaterialDesignH.HEART
            color = Constants.SPOTIFY_COLOR
        }
        changeIcon(likeIconButton, icon, color = color)
    }

    private fun changeIcon(button: JButton, icon: Ikon, size: Int = 25, color: Color = Constants.ICON_COLOR) {
        button.icon = FontIcon.of(icon, size, color)
        button.rolloverIcon = FontIcon.of(icon, size, color.brighter())
        button.repaint()
    }

    override fun update(playerData: PlayerDTO) {
        this.playerData = playerData
        updatePlayPause(playerData)
        updateShuffle(playerData)
        updateRepeatMode(playerData)
        repaint()
    }

    override fun update(track: TrackDTO) {
        spotifyApi.checkUsersSavedTracks(track.songId)
            .build()
            .executeAsync()
            .thenApply(fun(isLikedList) {
                updateLiked(isLikedList[0])
            })
    }

    override fun update(device: DeviceDTO?) {
        val isEnabled = device != null
        likeIconButton.isEnabled = isEnabled
        playPauseIconButton.isEnabled = isEnabled
        shuffleIconButton.isEnabled = isEnabled
        repeatIconButton.isEnabled = isEnabled
        stepBackward.isEnabled = isEnabled
        stepForward.isEnabled = isEnabled
    }
}
