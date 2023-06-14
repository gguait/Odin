package me.odin.commands.impl

import me.odin.Odin.Companion.miscConfig
import me.odin.commands.Command
import me.odin.features.general.HighLights
import me.odin.utils.skyblock.ChatUtils.modMessage

object HightlightCommand : Command("highlight", listOf("odhighlight")) {

    private inline val hightlightList get() = miscConfig.hightlightList

    override fun executeCommand(args: Array<String>) {
        if (args.isEmpty())
            modMessage("§cArguments empty. §fUsage: add, remove, clear, list")
        else {
            when (args[0]) {
                "add" -> {
                    if (args.size == 1) return modMessage("§cMissing mob name!")
                    val mobName = args.joinToString(1).lowercase()
                    if (hightlightList.contains(mobName)) return modMessage("§7$mobName §ris already on the highlight list.")

                    modMessage("Added §7$mobName §rto the highlight list.")
                    hightlightList.add(mobName.lowercase())
                    miscConfig.saveAllConfigs()
                }

                "remove" -> {
                    if (args.size == 1) return modMessage("§cMissing mob name!")
                    val mobName = args.joinToString(1)
                    if (!hightlightList.contains(mobName)) return modMessage("§7$mobName §risn't on the list.")

                    modMessage("Removed §7$mobName §rfrom the highlight list.")
                    hightlightList.remove(mobName)
                    miscConfig.saveAllConfigs()
                    HighLights.clear()
                }

                "clear" -> {
                    hightlightList.clear()
                    miscConfig.saveAllConfigs()
                    HighLights.clear()
                    modMessage("highlight List cleared.")
                }

                "list" -> hightlightList.forEach { modMessage(it) }

                "help" -> modMessage("Usage: add, remove, clear, list")

                else -> modMessage("§cIncorrect Usage. §rUsage: add, remove, clear, list")
            }
        }
    }
}