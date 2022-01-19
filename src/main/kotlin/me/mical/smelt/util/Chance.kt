package me.mical.smelt.util

import me.mical.smelt.api.Config
import me.mical.smelt.common.data.Item
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

/**
 * @author Ting
 * @date 2022/1/18 17:19
 */
fun inChance(chance: Double): Boolean {
    return (Math.random() * 100).toInt() <= chance
}

fun getRandom(type: String): Item {
    val map = Config.itemHash[type]!!
    val luck = map["luck"]!!.item
    val list = arrayListOf<Item>()
    val dataMap = hashMapOf<Item, Int>()
    var chanceTotal = 0
    map.values.forEach {
        if (!it.item.isSimilar(luck)) {
            list.add(it)
            dataMap[it] = it.chance
            chanceTotal += it.chance
        }
    }
    var select = Random.nextInt(chanceTotal)
    for (item in list) {
        select -= dataMap[item]!!
        if (select < 0) {
            return item
        }
    }
    return list[list.size - 1]
}