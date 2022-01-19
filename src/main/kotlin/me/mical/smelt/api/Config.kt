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

        private val irons = hashMapOf<String, Item>()
        private val golds = hashMapOf<String, Item>()
        private val diamonds = hashMapOf<String, Item>()

        val itemHash = hashMapOf<String, HashMap<String, Item>>()

        @Config(migrate = true, autoReload = true)
        lateinit var CONF: Configuration
            private set

        internal var INSTANCE = me.mical.smelt.api.Config()

        fun init() {
            itemHash["iron"] = irons
            itemHash["gold"] = golds
            itemHash["diamond"] = diamonds
            loadItems()
            CONF.onReload {
                INSTANCE = me.mical.smelt.api.Config()
                loadItems()
                console().sendInfo("Configuration-Auto-Reload")
            }
        }

        private fun loadItems() {
            itemHash.forEach { (_, v) -> v.clear() }
            listOf("iron", "gold", "diamond").forEach { type ->
                CONF.getConfigurationSection("items.$type")?.getKeys(false)?.forEach {
                    val check = CONF.getString("items.${type}.${it}.check")
                    val chance = CONF.getInt("items.${type}.${it}.chance")
                    val getchance = CONF.getInt("items.${type}.${it}.getchance")
                    val max = CONF.getInt("items.${type}.${it}.max")
                    val item = CONF.getItemStack("items.${type}.${it}.item")
                    if (check != null && item != null) {
                        val struct = Item(check, chance, getchance, max, item)
                        val typeHash = itemHash[type]
                        typeHash!![it] = struct
                        itemHash[type] = typeHash
                    }
                }
            }
        }

    }

}