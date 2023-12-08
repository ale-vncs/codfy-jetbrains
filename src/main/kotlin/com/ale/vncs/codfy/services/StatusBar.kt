package com.ale.vncs.codfy.services

import com.ale.vncs.codfy.dto.PlayerDTO
import com.ale.vncs.codfy.enum.SpotifyStatus
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyPlayerTrackObserver
import com.ale.vncs.codfy.notifier.SpotifyStatusObserver
import com.ale.vncs.codfy.utils.Constants
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.JBColor
import com.intellij.util.Consumer
import com.intellij.util.IconUtil
import java.awt.event.MouseEvent
import javax.swing.Icon

class StatusBar : StatusBarWidgetFactory {
    private lateinit var barWidget: StatusBarWidget
    private val statusBarName = Constants.APP_NAME

    override fun getId(): String {
        return statusBarName
    }

    override fun getDisplayName(): String {
        return statusBarName
    }

    override fun isAvailable(project: Project): Boolean {
        return true
    }

    override fun createWidget(project: Project): StatusBarWidget {
        barWidget = object : StatusBarWidget, SpotifyPlayerTrackObserver, SpotifyStatusObserver {
            private var statusBar: StatusBar? = null
            private var playerData: PlayerDTO? = null

            override fun dispose() {
                NotifierService.instance().removeSpotifyTrackerObserver(this)
                NotifierService.instance().removeSpotifyStatusObserver(this)
            }

            override fun ID(): String {
                return statusBarName
            }

            override fun install(statusBar: StatusBar) {
                NotifierService.instance().addSpotifyTrackerObserver(this)
                NotifierService.instance().addSpotifyStatusObserver(this)
                this.statusBar = statusBar
            }

            override fun getPresentation(): StatusBarWidget.WidgetPresentation {
                return object : StatusBarWidget.MultipleTextValuesPresentation {
                    override fun getTooltipText(): String {
                        return Constants.APP_NAME
                    }

                    override fun getClickConsumer(): Consumer<MouseEvent> {
                        return Consumer {}
                    }

                    override fun getPopup(): JBPopup? {
                        try {
                            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ID()) ?: return null

                            if (toolWindow.isVisible) toolWindow.hide()
                            else toolWindow.show()
                        } catch (_: Exception) {
                        }
                        return super.getPopup()
                    }

                    override fun getSelectedValue(): String {
                        if (playerData == null) return " No song playing"
                        return " ${playerData!!.songName} - ${playerData!!.songArtistName}"
                    }

                    override fun getIcon(): Icon {
                        val statusBarMaxHeight = 13f
                        var icon = IconLoader.getIcon(
                                "/icons/spotify-icon.svg",
                                this::class.java
                        )
                        icon = IconUtil.scale(icon, null, statusBarMaxHeight/icon.iconHeight)
                        if (playerData == null) icon = IconUtil.colorize(icon, JBColor.GRAY)
                        return icon
                    }
                }
            }

            override fun update(playerData: PlayerDTO) {
                this.playerData = playerData
                statusBar?.updateWidget(ID())
            }

            override fun update(status: SpotifyStatus) {
                this.playerData = null
                statusBar?.updateWidget(ID())
            }
        }
        return barWidget
    }

    override fun disposeWidget(widget: StatusBarWidget) {}

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return true
    }
}
