package com.ale.vncs.codfy.actions

import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.services.SpotifySendAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import org.kordamp.ikonli.materialdesign2.MaterialDesignP
import org.kordamp.ikonli.swing.FontIcon

class SpotifyPlayPauseAction : AnAction() {
    private val notifierService = NotifierService.instance()

    override fun actionPerformed(e: AnActionEvent) {
        SpotifySendAction.playPause()
    }

    override fun update(e: AnActionEvent) {
        val playerTrack = notifierService.getPlayerTrack()
        if (playerTrack?.isPlaying == true) {
            e.presentation.icon = FontIcon.of(MaterialDesignP.PAUSE_CIRCLE, 18, JBColor.BLACK)
            e.presentation.text = "Pause Song"
        } else {
            e.presentation.icon = FontIcon.of(MaterialDesignP.PLAY_CIRCLE, 18, JBColor.BLACK)
            e.presentation.text = "Play Song"
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
