package me.mical.smelt.util

import org.bukkit.inventory.ItemStack

/**
 * @author Ting
 * @date 2022/1/19 13:05
 */
fun checkType(item1: ItemStack, item2: ItemStack): Boolean {
    return item1.type.name == item2.type.name
}