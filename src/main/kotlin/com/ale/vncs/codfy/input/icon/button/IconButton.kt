package com.ale.vncs.codfy.input.icon.button

import com.ale.vncs.codfy.utils.Constants
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.swing.FontIcon
import java.awt.Cursor
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.JButton

open class IconButton(iconName: Ikon, size: Int = 18) : JButton() {
    init {
        isOpaque = false
        isContentAreaFilled = false
        icon = FontIcon.of(iconName, size, Constants.ICON_COLOR)
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        preferredSize = Dimension(size, size)
        rolloverIcon = FontIcon.of(iconName, size, Constants.ICON_COLOR_FOCUS)
        border = BorderFactory.createEmptyBorder()
        ignoreRepaint = true
        isFocusPainted = false
        background = null
        isBorderPainted = false
    }
}
