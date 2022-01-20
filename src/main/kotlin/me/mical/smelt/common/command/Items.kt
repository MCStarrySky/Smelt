package me.mical.smelt.common.command

import me.mical.smelt.api.Config
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.command

/**
 * @author Ting
 * @date 2022/1/20 21:21
 */
object Items {

    @Awake(LifeCycle.ENABLE)
    fun register() {
        command("items") {
            literal("get") {
                dynamic {
                    suggestion<ProxyPlayer> { _, _ ->
                        Config.itemHash.keys.toList()
                    }
                    dynamic {
                        suggestion<ProxyPlayer> { _, context ->
                            Config.itemHash[context.argument(1)]!!.keys.toList()
                        }
                        dynamic {
                            execute<Player> { sender, context, argument ->
                                val type = context.argument(1)
                                val item = context.argument(2)
                                val amount = argument.toInt()
                                if (Config.itemHash.containsKey(type)) {
                                    if (Config.itemHash[type]!!.containsKey(item)) {
                                        val itemStack = Config.itemHash[type]!![item]!!.item.clone()
                                        itemStack.amount = amount
                                        if (sender.inventory.firstEmpty() != -1) {
                                            sender.inventory.addItem(itemStack)
                                        } else {
                                            val out = sender.inventory.addItem(itemStack)
                                            for (entry in out.entries) {
                                                sender.world.dropItem(sender.location, entry.value)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}