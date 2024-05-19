@file:Suppress("UNCHECKED_CAST")

package com.mcstarrysky.smelt

import com.mcstarrysky.smelt.util.SimpleRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common5.*
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.getItemStack
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.getMap
import taboolib.module.configuration.util.mapSection

/**
 * @author Ting
 * @date 2022/1/18 22:45
 */
data class SmeltItem(
    val name: String,
    val registry: SimpleRegistry<ItemType, Item>
) {

    constructor(section: ConfigurationSection): this(
        section.name,
        object : SimpleRegistry<ItemType, Item>(
            section.mapSection { Item(it) }.mapKeys { ItemType.valueOf(it.key.uppercase()) }.toMutableMap()
        ) {
            override fun getKey(value: Item): ItemType {
                return value.type
            }
        }
    )

    companion object : SimpleRegistry<String, SmeltItem>(LinkedHashMap()) {

        @Config("items")
        private lateinit var items: Configuration

        override fun getKey(value: SmeltItem): String {
            return value.name
        }

        fun initialize() {
            clearRegistry()
            items.mapSection { SmeltItem(it) }.forEach { register(it.key, it.value) }
        }
    }
}

open class Item(
    val type: ItemType,
    val check: String,
    // 获取概率
    val chance: Int,
    val max: Int,
    val item: ItemStack,
    val params: Map<String, Any?>
) {

    constructor(section: ConfigurationSection) : this(
        ItemType.valueOf(section.name.uppercase()),
        section.getString("check")!!,
        section.getInt("chance"),
        section.getInt("max"),
        section.getItemStack("item")!!,
        section.getMap<String, Any?>("params"),
    )

    operator fun get(node: String): Any? = params[node]

    fun getInt(node: String): Int = this[node].cint

    fun getBoolean(node: String): Boolean = this[node].cbool

    fun getDouble(node: String): Double = this[node].cdouble

    fun getLong(node: String): Long = this[node].clong

    fun getStringList(node: String): List<String> = this[node] as List<String>

    // TODO: string, float, byte, char, short, etc.
}

enum class ItemType {

    IRON, GOLD, DIAMOND;

    companion object {

        fun matchItemType(material: Material): ItemType? {
            return when {
                "IRON" in material.name -> IRON
                "GOLD" in material.name -> GOLD
                "DIAMOND" in material.name -> DIAMOND
                else -> null
            }
        }
    }
}