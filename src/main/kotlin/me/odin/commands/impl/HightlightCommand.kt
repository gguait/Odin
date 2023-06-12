package me.odin.commands.impl

import me.odin.Odin.Companion.miscConfig
import me.odin.commands.Command
import me.odin.features.general.HighLights
import me.odin.utils.skyblock.ChatUtils.modMessage

object HightlightCommand : Command("esp", "odesp") {

    private inline val espList get() = miscConfig.espList

    override fun executeCommand(args: Array<String>) {
        if (args.isEmpty())
            modMessage("§cArguments empty. §fUsage: add, remove, clear, list")
        else {
            when (args[0]) {
                "add" -> {
                    if (args.size == 1) return modMessage("§cMissing mob name!")
                    val mobName = args.joinToString(1)
                    if (espList.contains(mobName)) return modMessage("$mobName is already on the ESP list.")

                    modMessage("Added $mobName to the ESP list.")
                    espList.add(mobName)
                    miscConfig.saveAllConfigs()
                }

                "remove" -> {
                    if (args.size == 1) return modMessage("§cMissing mob name!")
                    val mobName = args.joinToString(1)
                    if (!espList.contains(mobName)) return modMessage("$mobName isn't on the list.")

                    modMessage("Removed $mobName from the ESP list.")
                    espList.remove(mobName)
                    miscConfig.saveAllConfigs()
                    HighLights.clear()
                }

                "clear" -> {
                    espList.clear()
                    miscConfig.saveAllConfigs()
                    HighLights.clear()
                    modMessage("ESP List cleared.")
                }

                "list" -> espList.forEach { modMessage(it) }

                "help" -> modMessage("Usage: add, remove, clear, list")

                else -> modMessage("§cIncorrect Usage. §rUsage: add, remove, clear, list")
            }
        }
    }
}