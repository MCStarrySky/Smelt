package me.mical.smelt.common.module

import me.mical.smelt.api.Config
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.isAir
import taboolib.platform.util.sendWarn

/**
 * @author Ting
 * @date 2022/1/19 13:07
 */
object Jd {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun interact(e: PlayerInteractEvent) {
        if (e.hand == EquipmentSlot.HAND) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                e.isCancelled = true
                val item = e.player.inventory.getItem(0)
                if (item == null || item.isAir()) {
                    e.player.sendWarn("Jd-Null")
                    return
                }
                when (e.player.inventory.itemInMainHand.type) {
                    Material.IRON_INGOT -> {
                        val jd = Config.itemHash["iron"]!!["jd"]
                        if (jd != null) {

                        }
                    }
                    Material.GOLD_INGOT -> {

                    }
                    Material.DIAMOND -> {

                    }
                }
            }
        }
    }

}