package com.ale.vncs.codfy.actions

import com.ale.vncs.codfy.enum.SpotifyRepeatMode
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.services.SpotifySendAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import org.kordamp.ikonli.materialdesign2.MaterialDesignP
import org.kordamp.ikonli.materialdesign2.MaterialDesignR
import org.kordamp.ikonli.swing.FontIcon

class SpotifyRepeatModeAction : AnAction() {
    private val notifierService = NotifierService.instance()

    override fun actionPerformed(e: AnActionEvent) {
        SpotifySendAction.repeatMode()
    }

    override fun update(e: AnActionEvent) {
        val playerTrack = notifierService.getPlayerTrack()
        when(playerTrack?.repeatMode) {
            SpotifyRepeatMode.TRACK -> {
                e.presentation.icon = FontIcon.of(MaterialDesignR.REPEAT_OFF, 18, JBColor.BLACK)
                e.presentation.text = "No Repeat"
            }
            SpotifyRepeatMode.OFF -> {
                e.presentation.icon = FontIcon.of(MaterialDesignR.REPEAT, 18, JBColor.BLACK)
                e.presentation.text = "Repeat Playlist"
            }
            SpotifyRepeatMode.CONTEXT, null -> {
                e.presentation.icon = FontIcon.of(MaterialDesignR.REPEAT_ONCE, 18, JBColor.BLACK)
                e.presentation.text = "Repeat Song"
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return super.getActionUpdateThread()
    }
}
