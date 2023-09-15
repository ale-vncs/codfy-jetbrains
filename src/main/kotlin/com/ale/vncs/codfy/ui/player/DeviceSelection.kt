package com.ale.vncs.codfy.ui.player

import com.ale.vncs.codfy.component.GridBagPanel
import com.ale.vncs.codfy.dto.DeviceDTO
import com.ale.vncs.codfy.input.icon.button.IconButton
import com.ale.vncs.codfy.notifier.NotifierService
import com.ale.vncs.codfy.notifier.SpotifyDeviceChangeObserver
import com.ale.vncs.codfy.services.SpotifyDeviceService
import com.ale.vncs.codfy.utils.Constants
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.JBUI
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.materialdesign2.MaterialDesignC
import org.kordamp.ikonli.materialdesign2.MaterialDesignM
import org.kordamp.ikonli.materialdesign2.MaterialDesignR
import org.kordamp.ikonli.materialdesign2.MaterialDesignS
import org.kordamp.ikonli.swing.FontIcon
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.event.ItemEvent
import javax.swing.*


class DeviceSelection : GridBagPanel(), SpotifyDeviceChangeObserver {
    private var comboBoxModel = DefaultComboBoxModel<DeviceDTO>()
    private var comboBox = ComboBox(comboBoxModel)
    private val spotifyDeviceService = SpotifyDeviceService.instance()

    init {
        NotifierService.instance().addSpotifyDeviceChangeObserver(this)
        getDevices()
        createUI()
    }

    private fun getDevices() {
        spotifyDeviceService.reloadDevices().join()
        val devices = spotifyDeviceService.getDevices()
        comboBoxModel.removeAllElements()
        comboBoxModel.addAll(devices.toList())
        comboBoxModel.selectedItem = spotifyDeviceService.getDeviceSelected()
        repaint()
    }

    override fun createUI() {
        reloadButton()

        val comboBoxConstraint = getConstraint()

        comboBoxConstraint.weightx = 1.0
        comboBoxConstraint.fill = GridBagConstraints.HORIZONTAL

        comboBox.isUsePreferredSizeAsMinimum = false
        comboBox.isOpaque = true
        comboBox.isFocusable = false
        comboBox.addItemListener(fun(event) {
            if (event.stateChange == ItemEvent.SELECTED) {
                spotifyDeviceService.changeDevice(event.item as DeviceDTO)
            }
        })
        comboBox.setRenderer(CarListCellRenderer())

        add(comboBox, comboBoxConstraint)

    }

    private fun reloadButton() {
        val buttonConstraint = getConstraint()
        val button = IconButton(MaterialDesignR.RELOAD)

        buttonConstraint.gridx = 1
        buttonConstraint.anchor = GridBagConstraints.LINE_END
        buttonConstraint.insets = JBUI.insetsLeft(6)

        button.addActionListener(fun(_) {
            getDevices()
        })

        add(button, buttonConstraint)
    }

    internal class CarListCellRenderer : JLabel(), ListCellRenderer<DeviceDTO> {
        private fun getIcon(icon: Ikon): Icon {
            return FontIcon.of(icon, 18, Constants.ICON_COLOR)
        }

        override fun getListCellRendererComponent(
            list: JList<out DeviceDTO>?,
            value: DeviceDTO?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            val label = JLabel(value?.name ?: "No device selected")

            when(value?.type?.lowercase()) {
                "computer" -> label.icon = getIcon(MaterialDesignM.MONITOR)
                "smartphone" -> label.icon = getIcon(MaterialDesignC.CELLPHONE)
                "speaker" -> label.icon = getIcon(MaterialDesignS.SPEAKER)
            }

            label.iconTextGap = 8
            label.isOpaque = false

            return label
        }

    }

    override fun update(device: DeviceDTO?) {
        if (device == null) {
            comboBoxModel.removeAllElements()
            comboBoxModel.selectedItem = null
            repaint()
        }
    }
}
