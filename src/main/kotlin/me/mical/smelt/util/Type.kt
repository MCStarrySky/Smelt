package me.mical.smelt.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Ting
 * @date 2022/1/19 13:05
 */
fun checkType(item1: ItemStack, item2: ItemStack): Boolean {
    return item1.type.name == item2.type.name
}

fun getType(material: Material): String {
    val name = material.name
    return if (name.contains("IRON")) "iron" else if (name.contains("DIAMOND")) "diamond" else "gold"
}