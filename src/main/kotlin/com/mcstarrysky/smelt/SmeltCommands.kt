package com.mcstarrysky.smelt

import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.platform.util.giveItem

/**
 * @author Ting
 * @date 2022/1/20 21:21
 */
@CommandHeader("smelt")
object SmeltCommands {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val items = subCommand {
        literal("get") {
            dynamic {
                suggestion<ProxyPlayer> { _, _ ->
                    SmeltItem.keys.toList()
                }
                dynamic {
                    suggestion<ProxyPlayer> { _, context ->
                        SmeltItem[context.argument(-1)]!!.registry.keys.map { it.name.lowercase() }
                    }
                    dynamic {
                        execute<Player> { sender, context, argument ->
                            val type = context.argument(-2).uppercase()
                            val item = context.argument(-1)
                            val amount = argument.toInt()
                            val itemStack = SmeltItem[item]!!.registry[ItemType.valueOf(type)]!!.item.clone()
                            itemStack.amount = amount
                            sender.giveItem(itemStack)
                        }
                    }
                }
            }
        }
    }
}