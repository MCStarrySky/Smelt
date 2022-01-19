package me.mical.smelt.common.module

import me.mical.smelt.api.Config
import me.mical.smelt.util.getRandom
import me.mical.smelt.util.getType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendError
import taboolib.platform.util.sendInfo

/**
 * @author Ting
 * @date 2022/1/19 13:53
 */
object Luck {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun interact(e: PlayerInteractEvent) {
        if (e.hand == EquipmentSlot.HAND && e.action == Action.RIGHT_CLICK_BLOCK && isLuck(e.player.inventory.itemInMainHand)) {
            e.isCancelled = true
            if (e.player.inventory.firstEmpty() == -1) {
                e.player.sendError("Luck-Err")
                return
            }
            val result = getRandom(getType(e.player.inventory.itemInMainHand.type)).item
            if (e.player.inventory.itemInMainHand.amount <= 1) {
                e.player.inventory.setItemInMainHand(null)
            } else {
                e.player.inventory.itemInMainHand.amount = e.player.inventory.itemInMainHand.amount - 1
            }
            e.player.inventory.addItem(result)
            e.player.updateInventory()
            e.player.sendInfo("Luck-Get", result.itemMeta!!.displayName)
            e.player.world.playSound(e.player.location, Sound.BLOCK_ANVIL_HIT, 2.0f, 1.0f)
        }
    }

    private fun isLuck(item: ItemStack): Boolean {
        if (item.itemMeta == null || item.itemMeta!!.lore == null || item.itemMeta!!.lore!!.isEmpty()) return false
        when (item.type) {
            Material.IRON_INGOT -> {
                val luck = Config.itemHash["iron"]!!["luck"] ?: return false
                if (!item.itemMeta!!.lore!!.contains(luck.check)) return false
                return true
            }
            Material.GOLD_INGOT -> {
                val luck = Config.itemHash["gold"]!!["luck"] ?: return false
                if (!item.itemMeta!!.lore!!.contains(luck.check)) return false
                return true
            }
            Material.DIAMOND -> {
                val luck = Config.itemHash["diamond"]!!["luck"] ?: return false
                if (!item.itemMeta!!.lore!!.contains(luck.check)) return false
                return true
            }
        }
        return false
    }

}