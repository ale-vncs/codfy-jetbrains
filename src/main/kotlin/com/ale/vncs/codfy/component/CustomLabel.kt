package com.ale.vncs.codfy.component

import com.intellij.ui.components.JBLabel
import javax.swing.Icon
import javax.swing.plaf.basic.BasicLabelUI

open class CustomLabel() : JBLabel() {
    init {
        this.setUI(BasicLabelUI())
    }

    constructor(text: String) : this() {
        this.text = text
    }

    constructor(icon: Icon) : this() {
        this.icon = icon
    }

    constructor(text: String, horizontalAlignment: Int) : this() {
        this.text = text
        this.horizontalAlignment = horizontalAlignment
    }

    override fun updateUI() {}
}
