package com.ale.vncs.codfy.listener

import com.ale.vncs.codfy.utils.SpotifyTokens
import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor

class DynamicPlugin : DynamicPluginListener {
    override fun beforePluginUnload(pluginDescriptor: IdeaPluginDescriptor, isUpdate: Boolean) {
        SpotifyTokens.removeTokens()
        super.beforePluginUnload(pluginDescriptor, isUpdate)
    }
}
