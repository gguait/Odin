package me.odin.commands.impl

import me.odin.Odin.Companion.display
import me.odin.Odin.Companion.mc
import me.odin.commands.AbstractCommand
import me.odin.features.impl.render.ClickGUIModule
import me.odin.ui.clickgui.ClickGUI
import me.odin.ui.hud.EditHUDGui
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.ChatUtils.modMessage

object OdinCommand : AbstractCommand("odin", "od", "odin", description = "Main command for Odin.") {
    init {
        empty {
            display = ClickGUI
        }

        "edithud" {
            does {
                display = EditHUDGui
            }
        }

        "reset" {
            does {
                modMessage("Incorrect usage. Usage: clickgui, hud")
            }
            and(
                "clickgui" does {
                    ClickGUIModule.resetPositions()
                    modMessage("Reset click gui positions.")

                },
                "hud" does {
                    EditHUDGui.resetHUDs()
                    modMessage("Reset HUD positions.")
                }
            )
        }

        // TODO: make a command that sets both and make it not go above vanilla
        "setyaw" does {
            if (it.isEmpty()) return@does modMessage("§cMissing yaw!")
            val yaw = it.first().toFloatOrNull() ?: return@does modMessage("§cInvalid yaw!")
            mc.thePlayer.rotationYaw = yaw
            modMessage("Set yaw to $yaw.")
        }

        "setpitch" does {
            if (it.isEmpty()) return@does modMessage("§cMissing pitch!")
            val pitch = it.first().toFloatOrNull() ?: return@does modMessage("§cInvalid pitch!")
            mc.thePlayer.rotationPitch = pitch
            modMessage("Set pitch to $pitch.")
        }

        orElse {
            val arg = it.firstOrNull() ?: return@orElse modMessage("§cMissing argument!")
            if (arg.first() == 'f' || arg.first() == 'm') {
                if (arg.length != 2 || !arg[1].isDigit()) return@orElse modMessage("§cInvalid floorfr!")
                val type = it.first().first()
                val floor = numberMap[it.first()[1].digitToInt()] ?: return@orElse modMessage("§cInvalid floor!")
                val prefix = if (type == 'm') "master_" else ""
                ChatUtils.sendCommand("joininstance ${prefix}catacombs_floor_$floor ")
            } else {
                modMessage("§cInvalid floor!")
            }
        }

        "rq" {
            ChatUtils.sendCommand("instancerequeue")
            modMessage("requeing dungeon run")
        }


    }

    private val numberMap = mapOf(1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six", 7 to "seven")
}