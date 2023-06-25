package me.odin.commands.impl

import me.odin.Odin.Companion.display
import me.odin.commands.Command
import me.odin.Odin.Companion.mc
import me.odin.features.general.WaypointManager
import me.odin.ui.waypoint.WaypointGUI
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.skyblock.ChatUtils.partyMessage
import java.awt.Color
import java.util.*
import kotlin.math.floor

// TODO : REWORK COMMAND TO IMPLEMENT PERMANENT WAYPOINTS
object WaypointCommand : Command("waypoint", listOf("wp", "odwp")) {
    val randomColor: Color
        get() {
            val random = Random()
            val hue = random.nextFloat()

            val saturation = random.nextFloat() * 0.5f + 0.5f // High saturation
            val brightness = random.nextFloat() * 0.5f + 0.5f // High brightness

            val rgb = Color.HSBtoRGB(hue, saturation, brightness)
            val red = (rgb shr 16) and 0xFF
            val green = (rgb shr 8) and 0xFF
            val blue = rgb and 0xFF

            return Color(red, green, blue)
        }


    override fun executeCommand(args: Array<String>) {
        if (args.isEmpty()) return modMessage("§cArguments empty. §rUsage: gui, share, here, add, help")
        when (args[0]) {
            "help" -> sendHelpMessage()
            "gui" -> display = WaypointGUI

            "share" -> {
                val message = when (args.size) {
                    1 -> "x: ${floor(mc.thePlayer.posX).toInt()} y: ${floor(mc.thePlayer.posY).toInt()} z: ${floor(mc.thePlayer.posZ).toInt()}"
                    4 -> "x: ${args[1]} y: ${args[2]} z: ${args[3]}"
                    else -> return modMessage("§cInvalid arguments, §r/wp share (x y z).")
                }
                partyMessage(message)
            }

            "here" -> {
                if (args.size == 1) return modMessage("§cInvalid arguments, §r/wp here (temp | perm).")
                if (args[1] == "temp")
                    WaypointManager.addTempWaypoint(
                        "§fWaypoint",
                        floor(mc.thePlayer.posX).toInt(),
                        floor(mc.thePlayer.posY).toInt(),
                        floor(mc.thePlayer.posZ).toInt()
                    )
                else if (args[1] == "perm") {
                    WaypointManager.addWaypoint(
                        "§fWaypoint",
                        floor(mc.thePlayer.posX).toInt(),
                        floor(mc.thePlayer.posY).toInt(),
                        floor(mc.thePlayer.posZ).toInt(),
                        randomColor
                    )
                } else {
                    modMessage("§cInvalid arguments, §r/wp here (temp | perm).")
                    return
                }
                modMessage("Added Waypoint at ${floor(mc.thePlayer.posX).toInt()}, ${floor(mc.thePlayer.posY).toInt()}, ${floor(mc.thePlayer.posZ).toInt()}")
            }

            "add" -> {
                if (args.size >= 5) {
                    val values = args.getInt(2, 5) ?: return modMessage("§cInvalid arguments, §r/wp add (temp | perm) x y z.")
                    val name = if (args.size == 5) "Waypoint" else args[5]

                    if (args[1] == "temp")
                        WaypointManager.addTempWaypoint("§f$name", values[0], values[1], values[2])
                    else if (args[1] == "perm")
                        WaypointManager.addWaypoint("§f$name", values[0], values[1], values[2], randomColor)
                    else {
                        modMessage("§cInvalid arguments, §r/wp add (temp | perm) x y z.")
                        return
                    }
                    modMessage("Added ${if (args[1] == "temp") "temporary" else "permanent"} waypoint: $name at ${values.joinToString()}.")
                } else modMessage("§cInvalid arguments, §r/wp add (temp | perm) x y z.")
            }

            else -> sendHelpMessage()
        }
    }

    private fun sendHelpMessage() {
        modMessage(
            "§cInvalid usage, usage :\n" +
                    " - GUI » §7Opens the Gui \n" +
                    " - Share (x y z) » §7Sends a message with your current coords, unless coords are specified \n" +
                    " - Here (temp | perm) » §7Adds a permanent or temporary waypoint at your current coords\n" +
                    " - Add (temp | perm) x y z » §7Adds a permanent or temporary waypoint at the coords specified\n" +
                    " - Help » §7Shows this message"
        )
    }

    private fun Array<out String>.getInt(start: Int = 0, end: Int = this.size): Array<Int>? {
        val result = mutableListOf<Int>()
        for (i in start until end) {
            try {
                result.add(this[i].toInt())
            } catch (e: NumberFormatException) {
                return null
            }
        }
        return result.toTypedArray()
    }
}