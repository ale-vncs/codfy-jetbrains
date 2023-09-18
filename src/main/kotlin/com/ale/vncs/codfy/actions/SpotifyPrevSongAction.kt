package com.ale.vncs.codfy.actions

import com.ale.vncs.codfy.services.SpotifySendAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import org.kordamp.ikonli.materialdesign2.MaterialDesignS
import org.kordamp.ikonli.swing.FontIcon

class SpotifyPrevSongAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        SpotifySendAction.prevSong()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.icon = FontIcon.of(MaterialDesignS.STEP_BACKWARD, 18, JBColor.BLACK)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
