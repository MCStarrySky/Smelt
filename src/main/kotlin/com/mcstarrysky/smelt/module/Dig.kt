package com.mcstarrysky.smelt.module

import com.mcstarrysky.smelt.ItemType
import com.mcstarrysky.smelt.SmeltAPI
import com.mcstarrysky.smelt.SmeltItem
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.util.*

/**
 * @author Ting
 * @date 2022/1/20 17:37
 */
object Dig {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun e(e: PlayerInteractEvent) {
        if (e.isMainhand() && e.isRightClick()) {
            val item = e.player.inventory.getItem(0)
            val hand = e.player.inventory.itemInMainHand
            if (SmeltAPI.checkItem("dig", hand)) {
                e.isCancelled = true
                if (item == null || item.isAir()) {
                    e.player.sendWarn("Dig-Null")
                    return
                }
                val type = ItemType.matchItemType(hand.type) ?: return
                val dig = SmeltItem["dig"]?.registry?.get(type)
                val jd = SmeltItem["jd"]?.registry?.get(type)
                if (dig != null && jd != null) {
                    if (!SmeltAPI.checkType(hand, item) || !jd.getStringList("target").contains(item.type.name)) {
                        e.player.sendError("Dig-Err", hand.itemMeta!!.displayName, item.getI18nName(e.player))
                        return
                    }
                    val itemTag = item.getItemTag()
                    if (itemTag.getDeep("smelt.jd") == null) {
                        e.player.sendError("Dig-NoJd")
                        return
                    }
                    SmeltAPI.setEmptyStar(item, 1, e.player)
                }
            }
        }
    }
}