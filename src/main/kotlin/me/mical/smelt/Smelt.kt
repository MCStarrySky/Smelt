package me.mical.smelt

import me.mical.smelt.api.Config
import me.mical.smelt.util.Validate
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.disablePlugin
import taboolib.common.platform.function.pluginVersion
import taboolib.module.lang.sendError
import taboolib.module.lang.sendInfo
import taboolib.module.lang.sendInfoMessage
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

/**
 * @author Ting
 * @date 2022/1/18 16:34
 */
object Smelt : Plugin() {

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    private val validate = Validate.validate

    override fun onLoad() {
        console().sendLang("Plugin-Logo")
        console().sendLang("Plugin-Loading", Bukkit.getBukkitVersion())
    }


    override fun onEnable() {
        console().sendInfo("Plugin-BuildMac", Validate.buildMac())
        if (!validate) {
            console().sendError("Plugin-Unvalidated")
        } else {
            console().sendInfo("Plugin-Validated")
        }
        Config.init()
        console().sendInfo("Plugin-Enabled", pluginVersion)
        if (!validate) {
            disablePlugin()
        }
    }

}