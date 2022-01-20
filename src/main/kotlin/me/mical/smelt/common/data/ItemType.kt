package me.mical.smelt.common.data

/**
 * @author Ting
 * @date 2022/1/20 18:21
 */
object ItemType {

    val IRON_ARMOR = listOf("IRON_HELMET", "IRON_CHESTPLATE", "IRON_LEGGINGS", "IRON_BOOTS")
    val IRON_ARMS = listOf("IRON_SWORD")
    val IRON_TOOLS = listOf("IRON_SHOVEL", "IRON_PICKAXE", "IRON_AXE", "IRON_HOE")
    val GOLD_ARMOR = listOf(
        "GOLDEN_HELMET",
        "GOLDEN_CHESTPLATE",
        "GOLDEN_LEGGINGS",
        "GOLDEN_BOOTS",
        "CHAINMAIL_HELMET",
        "CHAINMAIL_CHESTPLATE",
        "CHAINMAIL_LEGGINGS",
        "CHAINMAIL_BOOTS"
    )
    val GOLD_ARMS = listOf("GOLDEN_SWORD", "BOW")
    val GOLD_TOOLS = listOf("GOLDEN_SHOVEL", "GOLDEN_PICKAXE", "GOLDEN_AXE", "GOLDEN_HOE")
    val DIAMOND_ARMOR = listOf("DIAMOND_HELMET", "DIAMOND_CHESTPLATE", "DIAMOND_LEGGINGS", "DIAMOND_BOOTS")
    val DIAMOND_ARMS = listOf("DIAMOND_SWORD")
    val DIAMOND_TOOLS = listOf("DIAMOND_SHOVEL", "DIAMOND_PICKAXE", "DIAMOND_AXE", "DIAMOND_HOE")
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