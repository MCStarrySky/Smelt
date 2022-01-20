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
        // 是否鉴定过武器, 是的话就是打孔操作
        if (itemTag.getDeep("jd") == null) {
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
            val starString = StringBuilder()
            with(starString) {
                append("§${Config.star_color_levels[star]}")
                for (i in 1..itemTag.getDeep("smelt.star").asInt()) {
                    append(Config.INSTANCE.star_empty)
                }
            }
            val meta = item.itemMeta
            meta?.lore = listOf("$starString")
            item.itemMeta = meta
            player.updateInventory()
            player.sendInfo("Jd")
            if (star >= Config.INSTANCE.jd_tipLevel) {
                Smelt.plugin.server.broadcastMessage(
                    console().asLangText(
                        "Jd1",
                        player.name,
                        star,
                        item.getI18nName()
                    )
                )
            }
            player.playSound(player.location, Sound.BLOCK_ANVIL_HIT, 2.0f, 1.0f)
        } else {
            if ((tempStar + itemTag.getDeep("smelt.star").asInt()) > jd.max) {
                // 如果孔位已打满

            } else if (itemTag.getDeep("smelt.empty").asInt() == 0) {
                // 如果没有空孔位

            } else {
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
                meta?.lore?.set(1, starString.toString())
                item.itemMeta = meta
                //TODO: 打孔成功的声音
            }
        }
    }

}