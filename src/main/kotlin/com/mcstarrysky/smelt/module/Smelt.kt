package com.mcstarrysky.smelt.module

import com.mcstarrysky.smelt.ItemType
import com.mcstarrysky.smelt.SmeltItem
import org.bukkit.block.Furnace
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.event.inventory.FurnaceSmeltEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir

/**
 * @author Ting
 * @date 2022/1/18 16:52
 */
object Smelt {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: FurnaceBurnEvent) {
        if (isSmelt(e.block.state as Furnace)) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: FurnaceSmeltEvent) {
        val furnace = e.block.state as Furnace
        if (isEmpty(furnace)) {
            val type = ItemType.matchItemType(e.source.type) ?: return
            val luck = SmeltItem["luck"]?.registry?.get(type) ?: return
            if (inChance(luck.getDouble("chance"))) {
                e.result = luck.item.clone()
            }
        } else if (isSmelt(furnace)) {
            e.isCancelled = true
        }
    }

    private fun isSmelt(furnace: Furnace): Boolean {
        val result = furnace.inventory.result
        if (result != null && result.isNotAir()) {
            val meta = result.itemMeta
            return meta != null && meta.displayName.isNotEmpty()
        }
        return false
    }

    private fun isEmpty(furnace: Furnace): Boolean {
        return furnace.inventory.result == null || furnace.inventory.result.isAir()
    }

    private fun inChance(chance: Double): Boolean {
        return (Math.random() * 100).toInt() <= chance
    }
}