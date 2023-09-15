package com.ale.vncs.codfy.ui.auth

import com.ale.vncs.codfy.utils.Constants
import com.ale.vncs.codfy.services.SpotifyService
import com.ale.vncs.codfy.component.DefaultPanel
import com.intellij.ui.components.ActionLink
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JLabel

class AuthPanel : DefaultPanel(GridBagLayout()) {
    private val appName = Constants.APP_NAME
    private val spotifyService = SpotifyService.instance()

    private val loginSpotifyButton = ActionLink("LogIn Spotify")

    init {
        textInfo()
        loginButton()
    }

    private fun textInfo() {
        val info = "You need logIn in Spotify to connect in $appName"
        val text = JLabel(info)
        add(text, GridBagConstraints())
    }

    private fun loginButton() {
        loginSpotifyButton.requestFocus()
        loginSpotifyButton.addActionListener(fun(_) {
            spotifyService.login()
        })

        val constraint = GridBagConstraints()

        constraint.gridy = 1
        constraint.ipady = 10
        constraint.insets = JBUI.insetsTop(10)

        add(loginSpotifyButton, constraint)
    }
}
