package me.mical.smelt.util

/**
 * @author Ting
 * @date 2022/1/18 17:19
 */
fun inChance(chance: Double): Boolean {
    return (Math.random() * 100).toInt() <= chance
}