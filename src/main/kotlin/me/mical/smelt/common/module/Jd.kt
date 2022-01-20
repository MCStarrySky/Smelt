package me.mical.smelt.common.module

import me.mical.smelt.Smelt
import me.mical.smelt.api.Config
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

    //FIXME: 后面写到的时候, 把所有操作孔位的代码写到一块, 直接调用函数.
    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun interact(e: PlayerInteractEvent) {
        if (e.hand == EquipmentSlot.HAND && e.action == Action.RIGHT_CLICK_BLOCK) {
            e.isCancelled = true
            val item = e.player.inventory.getItem(0)
            val hand = e.player.inventory.itemInMainHand
            if (isJd(hand)) {
                if (item == null || item.isAir()) {
                    e.player.sendWarn("Jd-Null")
                    return
                }
                val type = getType(hand.type)
                val jd = Config.itemHash[type]!!["jd"]
                if (jd != null) {
                    if (!checkType(hand, item)) {
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
                    if (e.player.inventory.itemInMainHand.amount <= 1) {
                        e.player.inventory.setItemInMainHand(null)
                    } else {
                        e.player.inventory.itemInMainHand.amount = e.player.inventory.itemInMainHand.amount - 1
                    }
                    if (Random.nextInt(100) >= jd.chance) {
                        e.player.updateInventory()
                        e.player.sendLang("Jd-Fail")
                        e.player.playSound(e.player.location, Sound.BLOCK_METAL_BREAK, 1.8f, 1.0f)
                        return
                    }
                    var star = getJDRandom()
                    if (star > jd.max) {
                        star = jd.max
                    }
                    val starString = StringBuilder()
                    for (i in 1..star) {
                        starString.append(Config.INSTANCE.star_empty)
                    }
                    val meta = item.itemMeta
                    meta?.lore = listOf("§${Config.star_color_levels[star]}${starString.toString()}")
                    item.itemMeta = meta
                    itemTag.putDeep("smelt.jd", true)
                    itemTag.putDeep("smelt.empty", star)
                    itemTag.saveTo(item)

                    e.player.updateInventory()
                    e.player.sendInfo("Jd")
                    if (star >= Config.INSTANCE.jd_tipLevel) {
                        Smelt.plugin.server.broadcastMessage(console().asLangText("Jd1", e.player.name, star, item.getI18nName()))
                    }
                    e.player.playSound(e.player.location, Sound.BLOCK_ANVIL_HIT, 2.0f, 1.0f)
                }
            }
        }
    }

    private fun isJd(item: ItemStack): Boolean {
        if (item.itemMeta == null || item.itemMeta!!.lore == null || item.itemMeta!!.lore!!.isEmpty()) return false
        when (item.type) {
            Material.IRON_INGOT -> {
                val jd = Config.itemHash["iron"]!!["jd"] ?: return false
                if (!item.itemMeta!!.lore!!.contains(jd.check)) return false
                return true
            }
            Material.GOLD_INGOT -> {
                val jd = Config.itemHash["gold"]!!["jd"] ?: return false
                if (!item.itemMeta!!.lore!!.contains(jd.check)) return false
                return true
            }
            Material.DIAMOND -> {
                val jd = Config.itemHash["diamond"]!!["jd"] ?: return false
                if (!item.itemMeta!!.lore!!.contains(jd.check)) return false
                return true
            }
        }
        return false
    }

}