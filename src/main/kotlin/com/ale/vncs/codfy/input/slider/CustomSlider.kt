package com.ale.vncs.codfy.input.slider

import com.intellij.ui.components.JBSlider

class CustomSlider : JBSlider() {
    init {
        setUI(CustomSliderUI())
        isOpaque = false
    }

    override fun updateUI() {}
}
