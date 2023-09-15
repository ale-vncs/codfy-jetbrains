package com.ale.vncs.codfy.input.slider

import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import java.awt.*
import javax.swing.JComponent
import javax.swing.JSlider
import javax.swing.plaf.basic.BasicSliderUI


class CustomSliderUI : BasicSliderUI() {
    override fun scrollDueToClickInTrack(direction: Int) {
        var value: Int = slider.value
        if (slider.orientation == JSlider.HORIZONTAL) {
            value = this.valueForXPosition(slider.getMousePosition().x)
        } else if (slider.orientation == JSlider.VERTICAL) {
            value = this.valueForYPosition(slider.getMousePosition().y)
        }
        slider.setValue(value)
        slider.addMouseListener(trackListener)
    }

    override fun scrollByUnit(direction: Int) {}
    override fun scrollByBlock(direction: Int) {}
    override fun paintFocus(g: Graphics) {}

    override fun getThumbSize(): Dimension {
        return THUMB_SIZE
    }

    override fun paint(g: Graphics, c: JComponent) {
        (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        super.paint(g, c)
    }

    override fun paintTrack(g: Graphics) {
        val g2 = g as Graphics2D
        val centerY = trackRect.centerY.toInt() - TRACK_HEIGHT / 2

        g2.color = if (JBColor.isBright()) Gray._177 else Gray._70
        g2.fillRoundRect(trackRect.x, centerY, trackRect.width, TRACK_HEIGHT, TRACK_ARC, TRACK_ARC)

        g2.color = JBColor.BLACK
        g2.fillRoundRect(trackRect.x, centerY, thumbRect.x, TRACK_HEIGHT, TRACK_ARC, TRACK_ARC)
    }

    override fun paintThumb(g: Graphics) {
        g.color = JBColor.BLACK
        g.fillOval(thumbRect.x, thumbRect.y, THUMB_SIZE.width, THUMB_SIZE.height)
    }

    companion object {
        const val TRACK_HEIGHT = 4
        const val TRACK_ARC = 4
        val THUMB_SIZE: Dimension = Dimension(16, 16)
    }
}
