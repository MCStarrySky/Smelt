package me.mical.smelt.common.module

import me.mical.smelt.api.Config
import me.mical.smelt.common.data.ItemType
import me.mical.smelt.util.checkType
import me.mical.smelt.util.getType
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import taboolib.platform.util.sendError
import taboolib.platform.util.sendWarn

/**
 * @author Ting
 * @date 2022/1/20 17:37
 */
object Dig {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun interact(e: PlayerInteractEvent) {
        if (e.hand == EquipmentSlot.HAND && e.action == Action.RIGHT_CLICK_BLOCK) {
            val item = e.player.inventory.getItem(0)
            val hand = e.player.inventory.itemInMainHand
            if (isDig(hand)) {
                e.isCancelled = true
                if (item == null || item.isAir()) {
                    e.player.sendWarn("Dig-Null")
                    return
                }
                val type = getType(hand.type)
                val dig = Config.itemHash[type]!!["dig"]
                if (dig != null) {
                    if (!checkType(hand, item) || !ItemType.JD[type]!!.contains(item.type.name)) {
                        e.player.sendError("Dig-Err", hand.itemMeta!!.displayName, item.getI18nName(e.player))
                        return
                    }
                }
                val itemTag = item.getItemTag()
                if (itemTag.getDeep("smelt.jd") == null) {
                    e.player.sendError("Dig-NoJd")
                    return
                }
                Star.setEmptyStar(item, 1, e.player)
            }
        }
    }

    private fun isDig(item: ItemStack): Boolean {
        if (item.itemMeta == null || item.itemMeta!!.lore == null ||item.itemMeta!!.lore!!.isEmpty()) return false
        val type = getType(item.type)
        val dig = Config.itemHash[type]!!["dig"] ?: return false
        return item.itemMeta!!.lore!!.contains(dig.check)
    }

}