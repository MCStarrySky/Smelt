package com.mcstarrysky.smelt

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.unsafeLazy
import taboolib.module.lang.sendInfo
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

/**
 * @author Ting
 * @date 2022/1/18 16:34
 */
object Smelt : Plugin() {

    val plugin by unsafeLazy {
        BukkitPlugin.getInstance()
    }

    override fun onLoad() {
        console().sendLang("Plugin-Logo")
        console().sendLang("Plugin-Loading", Bukkit.getBukkitVersion())
    }

    override fun onEnable() {
        SmeltItem.initialize()
        console().sendInfo("Plugin-Enabled", pluginVersion)
    }
}