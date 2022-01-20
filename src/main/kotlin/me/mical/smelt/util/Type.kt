package me.mical.smelt.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Ting
 * @date 2022/1/19 13:05
 */
fun checkType(item1: ItemStack, item2: ItemStack): Boolean {
    return getType(item1.type) == getType(item2.type)
}

fun getType(material: Material): String {
    val name = material.name
    return if (name.contains("IRON")) "iron" else if (name.contains("DIAMOND")) "diamond" else if (name.contains("GOLD")) "gold" else "unknown"
}