package me.mical.smelt.common.data

import org.bukkit.inventory.ItemStack

/**
 * @author Ting
 * @date 2022/1/18 22:45
 */
data class Item constructor(val check: String, val chance: Int, val max: Int, val item: ItemStack)