package com.ale.vncs.codfy.component

import java.awt.GridBagConstraints
import java.awt.GridBagLayout

abstract class GridBagPanel : DefaultPanel(GridBagLayout()) {
    protected fun getConstraint(): GridBagConstraints {
        val constraint =  GridBagConstraints()
        constraint.gridx = 0
        constraint.gridy = 0
        constraint.ipady = 0
        constraint.ipadx = 0
        return constraint
    }

    abstract fun createUI()
}
