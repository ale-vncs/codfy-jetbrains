package com.ale.vncs.codfy.actions

import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.services.SpotifySendAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import org.kordamp.ikonli.materialdesign2.MaterialDesignP
import org.kordamp.ikonli.materialdesign2.MaterialDesignS
import org.kordamp.ikonli.swing.FontIcon

class SpotifyShuffleSongAction : AnAction() {
    private val notifierService = NotifierService.instance()

    override fun actionPerformed(e: AnActionEvent) {
        SpotifySendAction.shuffleSong()
    }

    override fun update(e: AnActionEvent) {
        val playerTrack = notifierService.getPlayerTrack()
        e.presentation.icon = FontIcon.of(MaterialDesignS.SHUFFLE_VARIANT, 18, JBColor.BLACK)
        if (playerTrack?.isShuffle == true) {
            e.presentation.text = "Disable Shuffle"
        } else {
            e.presentation.text = "Enable Shuffle"
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return super.getActionUpdateThread()
    }
}
