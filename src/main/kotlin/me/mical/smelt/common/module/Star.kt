package me.mical.smelt.common.module

import me.mical.smelt.Smelt
import me.mical.smelt.api.Config
import me.mical.smelt.util.getJDRandom
import me.mical.smelt.util.getType
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.util.sendError
import taboolib.platform.util.sendInfo
import kotlin.random.Random

/**
 * @author Ting
 * @date 2022/1/20 12:52
 */
object Star {

    /**
     * 为武器设定星数.
     * @param item 物品堆.
     * @param star 星数.
     */
    fun setEmptyStar(item: ItemStack, star: Int, player: Player) {
        // 先获取物品的 ItemTag
        val itemTag = item.getItemTag()
        // 获取物品类型
        val type = getType(item.type)
        // 获取对应类型的鉴定石 Item 对象, 可以获得到此材质的最大星数
        val jd = Config.itemHash[type]!!["jd"]!!
        var tempStar = star
        // 是否鉴定过武器, 为 Null 的话就是鉴定操作
        if (itemTag.getDeep("smelt.jd") == null) {
            if (player.inventory.itemInMainHand.amount <= 1) {
                player.inventory.setItemInMainHand(null)
            } else {
                player.inventory.itemInMainHand.amount = player.inventory.itemInMainHand.amount - 1
            }
            if (Random.nextInt(100) >= jd.chance) {
                player.updateInventory()
                player.sendError("Jd-Fail")
                player.playSound(player.location, Sound.BLOCK_METAL_BREAK, 1.8f, 1.0f)
                return
            }
            // 设置鉴定的标识
            itemTag.putDeep("smelt.jd", true)
            tempStar = getJDRandom()
            // 判断是否超过该材质最大孔位数
            if (tempStar > jd.max) {
                tempStar = jd.max
            }
            // 设定总星数
            itemTag.putDeep("smelt.star", tempStar)
            // 设定空位星数
            itemTag.putDeep("smelt.empty", tempStar)
            // 设定已填星数
            itemTag.putDeep("smelt.fill", 0)
            // 写回物品堆
            itemTag.saveTo(item)
            // 构造孔的lore
            val starString = StringBuilder()
            with(starString) {
                append("§${Config.star_color_levels[tempStar]}")
                for (i in 1..itemTag.getDeep("smelt.star").asInt()) {
                    append(Config.INSTANCE.star_empty)
                }
            }
            // 写进物品堆
            val meta = item.itemMeta
            meta?.lore = listOf("$starString")
            item.itemMeta = meta
            player.updateInventory()
            player.sendInfo("Jd")
            // 全服公告
            if (tempStar >= Config.INSTANCE.jd_tipLevel) {
                Smelt.plugin.server.broadcastMessage(
                    console().asLangText(
                        "Jd1",
                        player.name,
                        tempStar,
                        item.getI18nName()
                    )
                )
            }
            player.playSound(player.location, Sound.BLOCK_ANVIL_HIT, 2.0f, 1.0f)
        } else {
            if ((tempStar + itemTag.getDeep("smelt.star").asInt()) > jd.max) {
                // 如果孔位已打满
                player.sendError("Dig-Max")
                return
            } else if (itemTag.getDeep("smelt.empty").asInt() == 0) {
                // 如果没有空孔位
                player.sendError("Dig-Full")
                return
            } else {
                val current = itemTag.getDeep("smelt.star").asInt()
                val map = hashMapOf<Int, Int>()
                Config.INSTANCE.dig_levels.forEach {
                    val level = it.split(' ')[0].toInt()
                    val chance = it.split(' ')[1].toInt()
                    map[level] = chance
                }
                if (!map.containsKey(current)) {
                    player.sendError("Dig-Unknown")
                    return
                }
                if (player.inventory.itemInMainHand.amount <= 1) {
                    player.inventory.setItemInMainHand(null)
                } else {
                    player.inventory.itemInMainHand.amount = player.inventory.itemInMainHand.amount - 1
                }
                val chance = map[current]!!
                if (Random.nextInt(100) < chance) {
                    // 写入数据
                    itemTag.putDeep("smelt.empty", itemTag.getDeep("smelt.empty").asInt() + tempStar)
                    itemTag.putDeep("smelt.star", itemTag.getDeep("smelt.star").asInt() + tempStar)
                    itemTag.saveTo(item)
                    val starString = StringBuilder()
                    with(starString) {
                        append("§${Config.star_color_levels[star]}")
                        for (i in 1..itemTag.getDeep("smelt.fill").asInt()) {
                            append(Config.INSTANCE.star_fill)
                        }
                        for (i in 1..itemTag.getDeep("smelt.empty").asInt()) {
                            append(Config.INSTANCE.star_empty)
                        }
                    }
                    val meta = item.itemMeta
                    val lore = meta?.lore
                    lore?.set(0, starString.toString())
                    meta?.lore = lore
                    item.itemMeta = meta
                    player.updateInventory()
                    player.sendInfo("Dig")
                    if ((current + tempStar) > Config.INSTANCE.dig_tipLevel) {
                        Smelt.plugin.server.broadcastMessage(
                            console().asLangText(
                                "Dig-Tip",
                                player.name,
                                current + tempStar,
                                item.getI18nName()
                            )
                        )
                    }
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 1.0f)
                    return
                }
                if ((itemTag.getDeep("smelt.star").asInt() - tempStar) > 0) {
                    itemTag.putDeep("smelt.star", itemTag.getDeep("smelt.star").asInt() - tempStar)

                    if ((itemTag.getDeep("smelt.empty").asInt() - tempStar) < 0) {
                        itemTag.putDeep("smelt.empty", 0)
                    } else {
                        itemTag.putDeep("smelt.empty", itemTag.getDeep("smelt.empty").asInt() - tempStar)
                    }
                    itemTag.saveTo(item)
                    val starString = StringBuilder()
                    with(starString) {
                        append("§${Config.star_color_levels[star]}")
                        for (i in 1..itemTag.getDeep("smelt.fill").asInt()) {
                            append(Config.INSTANCE.star_fill)
                        }
                        for (i in 1..itemTag.getDeep("smelt.empty").asInt()) {
                            append(Config.INSTANCE.star_empty)
                        }
                    }
                    val meta = item.itemMeta
                    val lore = meta?.lore
                    lore?.set(0, starString.toString())
                    meta?.lore = lore
                    item.itemMeta = meta
                } else {
                    itemTag.removeDeep("smelt.jd")
                    itemTag.removeDeep("smelt.effect")
                    itemTag.removeDeep("smelt.star")
                    itemTag.removeDeep("smelt.fill")
                    itemTag.removeDeep("smelt.empty")
                    itemTag.saveTo(item)
                    val meta = item.itemMeta
                    meta?.lore = listOf()
                    item.itemMeta = meta
                }
                player.updateInventory()
                player.sendError("Dig-Fail")
                player.playSound(player.location, Sound.BLOCK_METAL_BREAK, 1.8f, 1.0f)
            }
        }
    }

}