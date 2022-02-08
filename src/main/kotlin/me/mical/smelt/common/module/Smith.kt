package me.mical.smelt.common.module

import me.mical.smelt.api.Config
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.SmithItemEvent
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.SmithingRecipe
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getItemTag
import taboolib.platform.util.sendError
import taboolib.platform.util.sendInfo

/**
 * @author Ting
 * @date 2022/2/8 5:45 PM
 */
object Smith {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun smith(e: SmithItemEvent) {
        e.whoClicked as Player
        val smithItem = Config.itemHash["diamond"]!!["smith"] ?: return
        val smith = smithItem.item.clone()
        val need = Config.INSTANCE.smith_need

        val recipe = e.inventory.recipe as SmithingRecipe
        val item = (recipe.base as RecipeChoice.ExactChoice).itemStack.clone()
        val addition = (recipe.addition as RecipeChoice.ExactChoice).itemStack.clone()

        val itemTag = item.getItemTag()

        if (addition.isSimilar(smith) && addition.amount == need) {
            if (itemTag.getDeep("smelt.jd") == null) {
                e.isCancelled = true
                e.whoClicked.sendError("Smith-Err")
                return
            }
            e.inventory.result = item.also { it.type = Material.valueOf(it.type.name.replace("DIAMOND", "NETHERITE")) }
            e.whoClicked.sendInfo("Smith")
        } else {
            if (itemTag.getDeep("smelt.jd") != null) {
                e.isCancelled = true
                e.whoClicked.sendError("Smith-Exists")
            }
        }
    }

}