package com.ale.vncs.codfy.component

import java.awt.FlowLayout
import java.awt.LayoutManager
import javax.swing.BorderFactory
import javax.swing.JPanel

open class DefaultPanel(layout: LayoutManager = FlowLayout()) : JPanel() {
    init {
        isOpaque = false
        border = BorderFactory.createEmptyBorder()
        this.setLayout(layout)
    }
}
