package me.odin.ui.waypoint.elements

import cc.polyfrost.oneconfig.renderer.font.Fonts
import cc.polyfrost.oneconfig.utils.dsl.VG
import cc.polyfrost.oneconfig.utils.dsl.drawRoundedRect
import cc.polyfrost.oneconfig.utils.dsl.drawSVG
import cc.polyfrost.oneconfig.utils.dsl.nanoVG
import me.odin.features.general.WaypointManager
import me.odin.features.general.WaypointManager.Waypoint
import me.odin.ui.waypoint.WaypointGUI
import me.odin.ui.waypoint.WaypointGUI.mouseHandler
import me.odin.utils.gui.animations.ColorAnimation
import net.minecraft.util.StringUtils
import java.awt.Color

class WaypointElement(private val waypoint: Waypoint) {
    private val name get() = StringUtils.stripControlCodes(waypoint.name)
    private val colorAnimation = ColorAnimation(150)
    var y = 0f

    private val inputFields = arrayOf(
        InputField(name, mouseHandler, 12f, Fonts.REGULAR),
        InputField(waypoint.x, "x:", mouseHandler, 10f, Fonts.REGULAR),
        InputField(waypoint.y, "y:", mouseHandler, 10f, Fonts.REGULAR),
        InputField(waypoint.z, "z:", mouseHandler, 10f, Fonts.REGULAR),
    )

    fun drawScreen(vg: VG): Int {
        nanoVG(vg.instance) {
            drawRoundedRect(15, y, 450, 30, 5f, Color(13, 14, 15).rgb)

            val color = colorAnimation.getValue(waypoint.color, Color(21, 22, 23), waypoint.shouldShow).rgb
            drawRoundedRect(20, y + 6, 18, 18, 5f, color)
            drawSVG("/assets/odinclient/trash.svg", 442, y + 6, 18, 18, Color.WHITE.rgb, 100, javaClass)

            var currentX = 58f
            for (i in inputFields) {
                i.x = currentX
                currentX += i.draw(vg, currentX, y + 15, -1)
            }
        }
        return 40
    }

    fun mouseClicked(mouseButton: Int) {
        if (mouseButton == 0) {
            if (mouseHandler.isAreaHovered(15f, y, 18f, 30f)) {
                colorAnimation.start()
                waypoint.shouldShow = !waypoint.shouldShow
                return
            } else if (mouseHandler.isAreaHovered(442f, y + 6, 18f, 18f)) {
                WaypointManager.removeWaypoint(waypoint)
                WaypointGUI.updateElements()
            }

            for ((index, i) in inputFields.withIndex()) {
                if (!i.mouseClicked()) saveCurrent(index)
            }
        }
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        for ((index, i) in inputFields.withIndex()) {
            if (keyCode != 28) {
                i.keyTyped(typedChar, keyCode)
                continue
            }
            saveCurrent(index)
        }
    }

    private fun saveCurrent(index: Int) {
        val field = inputFields[index]
        field.listening = false
        val value = field.text.toIntOrNull() ?: 0
        if (index != 0) field.text = value.toString()
        when (index) {
            0 -> waypoint.name = "§f${field.text}"
            1 -> waypoint.x = value
            2 -> waypoint.y = value
            3 -> waypoint.z = value
        }
    }
}