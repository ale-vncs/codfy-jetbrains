package com.ale.vncs.codfy.services

import AlphaContainer
import com.ale.vncs.codfy.ui.MainContentPanel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class ToolWindow : ToolWindowFactory, DumbAware {
    private var toolWindow: ToolWindow? = null

    init {
        SpotifyService.instance().start()
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.toolWindow = toolWindow
        val toolWindowContent = MainContentPanel()
        val content = ContentFactory.getInstance().createContent(AlphaContainer(toolWindowContent), "", false)
        toolWindow.contentManager.addContent(content)
    }
}
