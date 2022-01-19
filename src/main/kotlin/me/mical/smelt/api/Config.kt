package me.mical.smelt.api

import me.mical.smelt.common.data.Item
import taboolib.common.platform.function.console
import taboolib.library.xseries.getItemStack
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.lang.sendInfo

/**
 * @author Ting
 * @date 2022/1/18 17:48
 */
class Config {

    companion object {

        val irons = hashMapOf<String, Item>()
        val golds = hashMapOf<String, Item>()
        val diamonds = hashMapOf<String, Item>()

        @Config(migrate = true, autoReload = true)
        lateinit var CONF: Configuration
            private set

        internal var INSTANCE = me.mical.smelt.api.Config()

        fun init() {
            loadItems()
            CONF.onReload {
                INSTANCE = me.mical.smelt.api.Config()
                loadItems()
                console().sendInfo("Configuration-Auto-Reload")
            }
        }

        private fun loadItems() {
            if (irons.isNotEmpty()) { irons.clear() }
            if (golds.isNotEmpty()) { golds.clear() }
            if (diamonds.isNotEmpty()) { diamonds.clear() }
            CONF.getConfigurationSection("items.iron")?.getKeys(false)?.forEach {
                val chance = CONF.getInt("items.iron.${it}.chance")
                val item = CONF.getItemStack("items.iron.${it}.item")
                if (item != null) {
                    val struct = Item(chance, item)
                    irons[it] = struct
                }
            }
            CONF.getConfigurationSection("items.gold")?.getKeys(false)?.forEach {
                val chance = CONF.getInt("items.gold.${it}.chance")
                val item = CONF.getItemStack("items.gold.${it}.item")
                if (item != null) {
                    val struct = Item(chance, item)
                    golds[it] = struct
                }
            }
            CONF.getConfigurationSection("items.diamond")?.getKeys(false)?.forEach {
                val chance = CONF.getInt("items.diamond.${it}.chance")
                val item = CONF.getItemStack("items.diamond.${it}.item")
                if (item != null) {
                    val struct = Item(chance, item)
                    diamonds[it] = struct
                }
            }
        }

    }

}