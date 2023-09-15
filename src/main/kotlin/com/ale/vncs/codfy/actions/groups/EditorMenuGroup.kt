package com.ale.vncs.codfy.actions.groups
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.utils.Constants
import com.intellij.openapi.actionSystem.AnActionEvent

import com.intellij.openapi.actionSystem.DefaultActionGroup


class EditorMenuGroup : DefaultActionGroup() {
    private val notifierService = NotifierService.instance()

    override fun update(event: AnActionEvent) {
        val isDevice = notifierService.getDevice() != null
        event.presentation.isEnabled = isDevice
        val msgInfo = if(!isDevice) " (No Device Selected)" else ""
        event.presentation.text = "${Constants.APP_NAME}$msgInfo"
    }
}
