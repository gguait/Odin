package me.odin.commands.impl

import me.odin.Odin.Companion.commandList
import me.odin.Odin.Companion.config
import me.odin.commands.Command
import me.odin.utils.skyblock.ChatUtils.modMessage

object OdinCommand : Command("odin", listOf("od", "odinmod")) {
    override fun executeCommand(args: Array<String>) {
        if (args.isEmpty())
            config.openGui()
        else {
            when (args[0]) {
                "help" -> modMessage("List of commands: ${commandList.joinToString { it.commandName }}")
                "blacklist" -> BlacklistCommand.executeCommand(args.copyOfRange(1, args.size))
                "highlight" -> HightlightCommand.executeCommand(args.copyOfRange(1, args.size))
                "waypoint" -> WaypointCommand.executeCommand(args.copyOfRange(1, args.size))
            }
        }
    }
}