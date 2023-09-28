package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.component.GridBagPanel
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import javax.swing.BorderFactory

class PlayerPanel : GridBagPanel() {
    private val songPeriodBar = SongPeriodBar()

    init {
        createUI()
    }

    override fun createUI() {
        val padding = 8
        val borderPadding = BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        val borderLined = BorderFactory.createLineBorder(JBColor.border(), 1, true)
        border = BorderFactory.createCompoundBorder(borderLined, borderPadding)

        deviceSelection()
        songDetail()
        songActions()
        songPeriodBar()
    }

    private fun deviceSelection() {
        val constraint = getConstraint()

        constraint.weightx = 1.0
        constraint.fill = GridBagConstraints.HORIZONTAL
        constraint.insets = JBUI.insetsBottom(4)
        add(DeviceSelection(), constraint)
    }

    private fun songDetail() {
        val constraint = getConstraint()

        constraint.weightx = 1.0
        constraint.gridheight = 2
        constraint.gridy = 1
        constraint.fill = GridBagConstraints.BOTH

        add(SongDetail(), constraint)
    }

    private fun songPeriodBar() {
        val constraint = getConstraint()
        constraint.gridx = 0
        constraint.gridy = 4
        constraint.weightx = 1.0
        constraint.fill = GridBagConstraints.HORIZONTAL

        add(songPeriodBar, constraint)
    }

    private fun songActions() {
        val constraint = getConstraint()
        constraint.gridx = 0
        constraint.gridy = 3
        constraint.weightx = 1.0
        constraint.fill = GridBagConstraints.HORIZONTAL

        add(SongActions(), constraint)
    }
}
