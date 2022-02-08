package me.mical.smelt.common.effect

import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Ting
 * @date 2022/1/18 16:47
 */
interface Effect {

    fun getId(): Int

    fun onAttack(event: EntityDamageByEntityEvent, data: Int)

    fun onBow(event: EntityDamageByEntityEvent, data: Int)

    fun onAttacked(event: EntityDamageByEntityEvent, data: Int, item: ItemStack)

    fun setEffectData(item: ItemStack, data: Int): ItemStack

}