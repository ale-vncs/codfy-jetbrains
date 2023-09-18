package com.ale.vncs.codfy.ui

import com.ale.vncs.codfy.component.CustomLabel
import com.ale.vncs.codfy.enum.SpotifyStatus
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyStatusObserver
import com.ale.vncs.codfy.services.SpotifyService
import com.ale.vncs.codfy.component.GridBagPanel
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.ui.components.JBLabel
import com.intellij.util.ImageLoader
import com.intellij.util.ui.ImageUtil
import com.intellij.util.ui.ImageUtil.toBufferedImage
import org.imgscalr.Scalr
import java.awt.GridBagConstraints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.SwingConstants

class Header(private val spotifyStatus: SpotifyStatus) : GridBagPanel() {
    private val spotifyService = SpotifyService.instance()

    init {
        createUI()
    }

    override fun createUI() {
        spotifyLogo()
        userIcon()
    }

    private fun spotifyLogo() {
        val constraints = getConstraint()

        constraints.weightx = 1.0
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.anchor = GridBagConstraints.LINE_START

        var image = ImageLoader.loadFromResource("/icons/spotify-logo.svg", javaClass)
        image = Scalr.resize(toBufferedImage(image!!), 100)
        val label = CustomLabel(ImageIcon(image))
        label.horizontalAlignment = SwingConstants.LEFT

        add(label, constraints)
    }

    private fun userIcon() {
        if (spotifyStatus != SpotifyStatus.LOGGED) return
        val user = spotifyService.getUser()

        val constraints = getConstraint()
        constraints.gridx = 1

        val icon = JBLabel()
        val menu = iconMenu()

        val userImage = ImageIO.read(URI.create(user.images[0].url).toURL())
        var image = Scalr.resize(userImage, 30)
        image = ImageUtil.createRoundedImage(image, 50.0)
        icon.icon = ImageIcon(image)

        icon.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val label = e.source as JLabel
                menu.show(label, -(label.width + 10), label.height + 10)
            }
        })
        icon.add(menu)
        add(icon, constraints)
    }

    private fun iconMenu(): JBPopupMenu {
        val menu = JBPopupMenu()

        val logoutItem = JBMenuItem("Logout")

        logoutItem.addActionListener(fun(_) {
            spotifyService.logout()
        })

        menu.add(logoutItem)

        return menu
    }
}
