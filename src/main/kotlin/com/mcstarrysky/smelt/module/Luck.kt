package com.mcstarrysky.smelt.module

import com.mcstarrysky.smelt.ItemType
import com.mcstarrysky.smelt.SmeltAPI
import org.bukkit.Sound
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.*

/**
 * @author Ting
 * @date 2022/1/19 13:53
 */
object Luck {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: PlayerInteractEvent) {
        if (e.isRightClick() && e.isMainhand() && SmeltAPI.checkItem("jd", e.player.inventory.itemInMainHand)) {
            e.isCancelled = true
            if (e.player.inventory.firstEmpty() == -1) {
                e.player.sendError("Luck-Err")
                return
            }
            val result = (SmeltAPI.getRandomItem(ItemType.matchItemType(e.player.inventory.itemInMainHand.type) ?: return) ?: return).item.clone()
            if (e.player.inventory.itemInMainHand.amount <= 1) {
                e.player.inventory.setItemInMainHand(null)
            } else {
                e.player.inventory.itemInMainHand.amount -= 1
            }
            e.player.giveItem(result)
            e.player.updateInventory()
            e.player.sendInfo("Luck-Get", result.itemMeta!!.displayName)
            e.player.world.playSound(e.player.location, Sound.BLOCK_ANVIL_HIT, 2.0f, 1.0f)
        }
    }
}