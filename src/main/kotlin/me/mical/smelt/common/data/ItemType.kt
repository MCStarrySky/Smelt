package me.mical.smelt.common.data

/**
 * @author Ting
 * @date 2022/1/20 18:21
 */
object ItemType {

    private val JD_IRON = listOf("IRON_SWORD", "IRON_HELMET", "IRON_CHESTPLATE", "IRON_LEGGINGS", "IRON_BOOTS")
    private val JD_GOLD =
        listOf(
            "BOW",
            "DIAMOND_SWORD",
            "GOLDEN_HELMET",
            "GOLDEN_CHESTPLATE",
            "GOLDEN_LEGGINGS",
            "GOLDEN_BOOTS",
            "CHAINMAIL_HELMET",
            "CHAINMAIL_CHESTPLATE",
            "CHAINMAIL_LEGGINGS",
            "CHAINMAIL_BOOTS"
        )
    private val JD_DIAMOND = listOf("DIAMOND_SWORD", "DIAMOND_HELMET", "DIAMOND_CHESTPLATE", "DIAMOND_LEGGINGS", "DIAMOND_BOOTS")

    val JD: HashMap<String, List<String>> = object : HashMap<String, List<String>>() {
        init {
            put("iron", JD_IRON)
            put("gold", JD_GOLD)
            put("diamond", JD_DIAMOND)
        }
    }

}