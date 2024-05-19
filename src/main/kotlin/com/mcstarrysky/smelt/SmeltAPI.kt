package com.mcstarrysky.smelt

import com.mcstarrysky.smelt.util.SmeltRandomList
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common5.RandomList
import taboolib.common5.cint
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.util.isAir
import taboolib.platform.util.sendError
import taboolib.platform.util.sendInfo
import kotlin.random.Random

/**
 * Smelt
 * me.mical.smelt.SmeltAPI
 *
 * @author mical
 * @since 2024/5/19 19:43
 */
object SmeltAPI {

    /**
     * 检查某物品是否是某个宝石
     */
    fun checkItem(name: String, item: ItemStack?): Boolean {
        if (item == null || item.isAir) return false
        if (item.itemMeta == null || item.itemMeta!!.lore == null || item.itemMeta!!.lore!!.isEmpty()) return false
        val type = ItemType.matchItemType(item.type) ?: return false
        val smeltItem = SmeltItem[name]?.registry?.get(type) ?: return false
        return item.itemMeta?.lore?.contains(smeltItem.check) == true
    }

    fun checkType(a: ItemStack?, b: ItemStack?): Boolean {
        return ItemType.matchItemType(a?.type ?: return false) ==
                ItemType.matchItemType(b?.type ?: return false)
    }

    fun getRandomItem(type: ItemType): Item? {
        val smeltsMap = SmeltItem.filterKeys { it != "luck" }.values.map { it.registry.values }.flatten().filter { it.type == type }
        if (smeltsMap.isEmpty()) return null
        val randomList = SmeltRandomList(smeltsMap.map { it to it.chance })
        return randomList.random()
    }

    /**
     * 为武器设定星数.
     * @param item 物品堆.
     * @param star 星数.
     */
    fun setEmptyStar(item: ItemStack, star: Int, player: Player) {
        // 先获取物品的 ItemTag
        val itemTag = item.getItemTag()
        // 获取对应类型的鉴定石 Item 对象, 可以获得到此材质的最大星数
        val jd = SmeltItem["jd"]!!.registry[ItemType.matchItemType(item.type) ?: return]!!
        var tempStar = star
        // 是否鉴定过武器, 为 Null 的话就是鉴定操作
        if (itemTag.getDeep("smelt.jd") == null) {
            if (player.inventory.itemInMainHand.amount <= 1) {
                player.inventory.setItemInMainHand(null)
            } else {
                player.inventory.itemInMainHand.amount -= 1
            }
            if (Random.nextInt(100) >= jd.chance) {
                player.updateInventory()
                player.sendError("Jd-Fail")
                player.playSound(player.location, Sound.BLOCK_METAL_BREAK, 1.8f, 1.0f)
                return
            }
            tempStar = RandomList(SmeltSettings.jd_levels.toList()).random() ?: return
            // 设置鉴定的标识
            itemTag.putDeep("smelt.jd", true)
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
                append(SmeltSettings.star_colors[tempStar]?.colored())
                for (i in 1..itemTag.getDeep("smelt.star")!!.asInt()) {
                    append(SmeltSettings.star_empty)
                }
            }
            // 写进物品堆
            val meta = item.itemMeta
            meta?.lore = listOf("$starString")
            item.itemMeta = meta
            player.updateInventory()
            player.sendInfo("Jd")
            // 全服公告
            if (tempStar >= SmeltSettings.jd_tip_level) {
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
            if ((tempStar + itemTag.getDeep("smelt.star").cint) > jd.max) {
                // 如果孔位已打满
                player.sendError("Dig-Max")
                return
            } else if (itemTag.getDeep("smelt.empty").cint == 0) {
                // 如果没有空孔位
                player.sendError("Dig-Full")
                return
            } else {
                val current = itemTag.getDeep("smelt.star").cint
                if (!SmeltSettings.dig_levels.containsKey(current)) {
                    player.sendError("Dig-Unknown")
                    return
                }
                if (player.inventory.itemInMainHand.amount <= 1) {
                    player.inventory.setItemInMainHand(null)
                } else {
                    player.inventory.itemInMainHand.amount -= 1
                }
                val chance = SmeltSettings.dig_levels[current]!!
                if (Random.nextInt(100) < chance) {
                    // 写入数据
                    itemTag.putDeep("smelt.empty", itemTag.getDeep("smelt.empty").cint + tempStar)
                    itemTag.putDeep("smelt.star", itemTag.getDeep("smelt.star").cint + tempStar)
                    itemTag.saveTo(item)
                    val starString = StringBuilder()
                    with(starString) {
                        append(SmeltSettings.star_colors[current + tempStar]?.colored())
                        for (i in 1..itemTag.getDeep("smelt.fill").cint) {
                            append(SmeltSettings.star_fill)
                        }
                        for (i in 1..itemTag.getDeep("smelt.empty").cint) {
                            append(SmeltSettings.star_empty)
                        }
                    }
                    val meta = item.itemMeta
                    val lore = meta?.lore
                    lore?.set(0, starString.toString())
                    meta?.lore = lore
                    item.itemMeta = meta
                    player.updateInventory()
                    player.sendInfo("Dig")
                    if ((current + tempStar) > SmeltSettings.dig_tip_level) {
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
                if ((itemTag.getDeep("smelt.star").cint - tempStar) > 0) {
                    itemTag.putDeep("smelt.star", itemTag.getDeep("smelt.star").cint - tempStar)

                    if ((itemTag.getDeep("smelt.empty").cint - tempStar) < 0) {
                        itemTag.putDeep("smelt.empty", 0)
                    } else {
                        itemTag.putDeep("smelt.empty", itemTag.getDeep("smelt.empty").cint - tempStar)
                    }
                    itemTag.saveTo(item)
                    val starString = StringBuilder()
                    with(starString) {
                        append(SmeltSettings.star_colors[current - tempStar]?.colored())
                        for (i in 1..itemTag.getDeep("smelt.fill").cint) {
                            append(SmeltSettings.star_fill)
                        }
                        for (i in 1..itemTag.getDeep("smelt.empty").cint) {
                            append(SmeltSettings.star_empty)
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