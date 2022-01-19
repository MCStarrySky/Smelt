package me.mical.smelt.common.module

import me.mical.smelt.api.Config
import me.mical.smelt.util.inChance
import org.bukkit.Material
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
    fun burn(e: FurnaceBurnEvent) {
        if (isSmelt(e.block.state as Furnace)) {
            e.isCancelled = true
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun smelt(e: FurnaceSmeltEvent) {
        val furnace = e.block.state as Furnace
        if (isEmpty(furnace)) {
            when (e.source.type) {
                Material.DEEPSLATE_IRON_ORE, Material.IRON_ORE, Material.RAW_IRON -> {
                    val struct = Config.itemHash["iron"]!!["luck"]
                    if (struct != null) {
                        if (inChance(struct.chance.toDouble())) {
                            e.result = struct.item
                        }
                    }
                }
                Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE, Material.GOLD_ORE, Material.RAW_GOLD -> {
                    val struct = Config.itemHash["gold"]!!["luck"]
                    if (struct != null) {
                        if (inChance(struct.chance.toDouble())) {
                            e.result = struct.item
                        }
                    }
                }
                Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND_ORE -> {
                    val struct = Config.itemHash["diamond"]!!["luck"]
                    if (struct != null) {
                        if (inChance(struct.chance.toDouble())) {
                            e.result = struct.item
                        }
                    }
                }
                else -> {}
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

}