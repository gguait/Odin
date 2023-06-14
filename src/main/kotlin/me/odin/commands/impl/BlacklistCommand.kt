package me.odin.commands.impl

import me.odin.Odin.Companion.miscConfig
import me.odin.commands.Command
import me.odin.utils.skyblock.ChatUtils.modMessage

object BlacklistCommand : Command("blacklist", listOf("odblacklist")) {
    override fun executeCommand(args: Array<String>) {
        if (args.isEmpty())
            modMessage("§cArguments empty. §fUsage: add, remove, clear, list")
        else {
            when (args[0]) {
                "add" -> {
                    if (args.size == 1) return modMessage("You need to name someone to add to the Blacklist.")
                    val playerName = args[1].lowercase()
                    if (miscConfig.blacklist.contains(playerName)) return modMessage("§7$playerName §ris already in the Blacklist.")

                    modMessage("Added §7$playerName §rto Blacklist.")
                    miscConfig.blacklist.add(playerName)
                    miscConfig.saveAllConfigs()
                }

                "remove" -> {
                    if (args.size == 1) return modMessage("You need to name someone to remove from the Blacklist.")
                    val playerName = args[1]
                    if (!miscConfig.blacklist.contains(playerName)) return modMessage("§7$playerName §risn't in the Blacklist.")

                    modMessage("Removed §7$playerName §rfrom Blacklist.")
                    miscConfig.blacklist.remove(playerName)
                    miscConfig.saveAllConfigs()
                }

                "clear" -> {
                    modMessage("Blacklist cleared.")
                    miscConfig.blacklist.clear()
                    miscConfig.saveAllConfigs()
                }

                "list" -> miscConfig.blacklist.forEach { modMessage(it) }
                else -> modMessage("Blacklist incorrect usage. Usage: add, remove, clear, list")
            }
        }
    }
}