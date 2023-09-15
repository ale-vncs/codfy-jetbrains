package com.ale.vncs.codfy.utils

import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.util.ImageLoader
import java.awt.Color
import java.awt.Image

object Constants {
    const val APP_NAME = Secret.APP_NAME
    val SPOTIFY_COLOR = Color(30, 215, 96)
    val BORDER_COLOR = JBColor.border()
    val ICON_COLOR: Color = JBColor.DARK_GRAY
    val ICON_COLOR_FOCUS: Color get() = if (JBColor.isBright()) Gray._100 else Gray._255
    val TIME_FONT_COLOR: Color = Gray._179
    val FALLBACK_SONG_IMAGE: Image get() = ImageLoader.loadFromResource("/icons/spotify-icon.svg", javaClass)!!
}
