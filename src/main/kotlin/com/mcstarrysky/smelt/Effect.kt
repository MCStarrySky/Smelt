package com.mcstarrysky.smelt

import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Ting
 * @date 2022/1/18 16:47
 */
abstract class Effect(
    type: ItemType,
    check: String,
    // 获取概率
    chance: Int,
    max: Int,
    item: ItemStack,
    params: Map<String, Any?>
) : Item(type, check, chance, max, item, params) {

    abstract fun getId(): Int

    open fun onAttack(event: EntityDamageByEntityEvent, data: Int) {
    }

    open fun onBow(event: EntityDamageByEntityEvent, data: Int) {
    }

    open fun onAttacked(event: EntityDamageByEntityEvent, data: Int, item: ItemStack) {
    }

    open fun setEffectData(item: ItemStack, data: Int): ItemStack {
        return item
    }
}