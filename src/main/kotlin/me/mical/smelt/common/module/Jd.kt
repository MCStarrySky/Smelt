package me.mical.smelt.common.module

import me.mical.smelt.Smelt
import me.mical.smelt.api.Config
import me.mical.smelt.common.data.ItemType
import me.mical.smelt.util.checkType
import me.mical.smelt.util.getJDRandom
import me.mical.smelt.util.getType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.server
import taboolib.module.lang.asLangText
import taboolib.module.lang.asQualifyText
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag
import taboolib.platform.util.*
import kotlin.random.Random

/**
 * @author Ting
 * @date 2022/1/19 13:07
 */
object Jd {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun interact(e: PlayerInteractEvent) {
        if (e.hand == EquipmentSlot.HAND && e.action == Action.RIGHT_CLICK_BLOCK) {
            val item = e.player.inventory.getItem(0)
            val hand = e.player.inventory.itemInMainHand
            if (isJd(hand)) {
                e.isCancelled = true
                if (item == null || item.isAir()) {
                    e.player.sendWarn("Jd-Null")
                    return
                }
                val type = getType(hand.type)
                val jd = Config.itemHash[type]!!["jd"]
                if (jd != null) {
                    if (!checkType(hand, item) || !ItemType.JD[type]!!.contains(item.type.name)) {
                        e.player.sendError("Jd-Err", hand.itemMeta!!.displayName, item.getI18nName(e.player))
                        return
                    }
                    val itemTag = item.getItemTag()
                    if (itemTag.getDeep("smelt.jd") != null) {
                        e.player.sendError("Jd-Exists")
                        return
                    }
                    if (itemTag.getDeep("smelt.effect") != null) {
                        e.player.sendError("Jd-Effect")
                        return
                    }
                    Star.setEmptyStar(item, 0, e.player)
                }
            }
        }
    }

    private fun isJd(item: ItemStack): Boolean {
        if (item.itemMeta == null || item.itemMeta!!.lore == null || item.itemMeta!!.lore!!.isEmpty()) return false
        val type = getType(item.type)
        val jd = Config.itemHash[type]!!["jd"] ?: return false
        return item.itemMeta!!.lore!!.contains(jd.check)
    }

}