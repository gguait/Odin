package me.odin.ui.waypoint

import cc.polyfrost.oneconfig.renderer.font.Fonts
import cc.polyfrost.oneconfig.utils.dsl.*
import me.odin.features.general.WaypointManager
import me.odin.features.general.WaypointManager.Waypoint
import me.odin.utils.gui.MouseHandler
import me.odin.utils.gui.animations.ColorAnimation
import net.minecraft.util.StringUtils
import java.awt.Color
import java.lang.Exception

class WaypointElement(private val waypoint: Waypoint) {
    private val name get() = StringUtils.stripControlCodes(waypoint.name)
    private val colorAnimation = ColorAnimation(150)
    var y = 0f
    private var w1 = 0f
    private var w2 = 0f
    private var w3 = 0f
    private var w4 = 0f

    private val darkGray = Color(30, 32, 34).rgb

    fun drawScreen(vg: VG): Int {
        nanoVG(vg.instance) {
            drawRoundedRect(15, y, 450, 30, 5f,  Color(13, 14, 15).rgb)

            val color = colorAnimation.getValue(waypoint.color, Color(21, 22, 23), waypoint.shouldShow).rgb

            drawRoundedRect(20, y + 6, 18, 18, 5f, color)
            drawSVG("/assets/odin/trash.svg", 442, y + 6, 18, 18, Color.WHITE.rgb, 100, javaClass)

            w4 = getTextWidth(name, 12f, Fonts.REGULAR)
            if (listening == 4) drawText(current, 50, y + 15, Color.WHITE.rgb, 12f, Fonts.REGULAR)
            else drawText(name, 50, y + 15, Color.WHITE.rgb, 12f, Fonts.REGULAR)

            w1 = getTextWidth("z: ${waypoint.z}", 10f, Fonts.REGULAR)
            w2 = getTextWidth("y: ${waypoint.y}", 10f, Fonts.REGULAR)
            w3 = getTextWidth("x: ${waypoint.x}", 10f, Fonts.REGULAR)
            if (listening == 1) drawText("x: $current", xRender, y + 15, -1, 10f, Fonts.REGULAR)
            else drawText("x: ${waypoint.x}", xRender, y + 15, -1, 10f, Fonts.REGULAR)

            if (listening == 2) drawText("y: $current", yRender, y + 15, -1, 10f, Fonts.REGULAR)
            else drawText("y: ${waypoint.y}", yRender, y + 15, -1, 10f, Fonts.REGULAR)

            if (listening == 3) drawText("z: $current", zRender, y + 15, -1, 10f, Fonts.REGULAR)
            else drawText("z: ${waypoint.z}", zRender, y + 15, -1, 10f, Fonts.REGULAR)

            when (listening) {
                1 -> {
                    drawHollowRoundedRect(xRender - 6, y + 5, w3 + 12, 20, 5f, darkGray, 1f) // no thicker is bnad
                }
                2 -> {
                    drawHollowRoundedRect(yRender - 6, y + 5, w2 + 12, 20, 5f, darkGray, 1f)
                }
                3 -> {
                    drawHollowRoundedRect(zRender - 6, y + 5, w1 + 12, 20, 5f, darkGray, 1f)
                }

                4 -> {
                    drawHollowRoundedRect(45, y + 5, w4 + 12, 20, 5f, darkGray, 1f)
                }
            }
        }
        return 40
    }

    var listening = 0
    fun mouseClicked(mouseButton: Int, mouseHandler: MouseHandler) {
        if (mouseButton == 0) {
            if (mouseHandler.isAreaHovered(15f, y, 18f, 30f)) {
                colorAnimation.start()
                waypoint.shouldShow = !waypoint.shouldShow
                return
            } else if (mouseHandler.isAreaHovered(442f, y + 6, 18f, 18f)) {
                WaypointManager.removeWaypoint(waypoint)
                WaypointGUI.updateElements()
            }

            listening = if (mouseHandler.isAreaHovered(xRender, y + 7.5f, w3, 15f)) 1
            else if (mouseHandler.isAreaHovered(yRender, y + 7.5f, w2, 15f)) 2
            else if (mouseHandler.isAreaHovered(zRender, y + 7.5f, w1, 15f)) 3
            else if (mouseHandler.isAreaHovered(45f, y + 7.5f, w4 + 5, 15f)) 4
            else {
                saveCurrent(listening)
                0
            }
        }
    }

    private var current = ""
    fun keyTyped(typedChar: Char, keyCode: Int) {
        if (listening == 0) return
        if (listening == 4) {
            when (keyCode) {
                14 -> if (current.isNotEmpty()) current = current.dropLast(1)

                28 -> saveCurrent(listening)

                else -> current = current.plus(typedChar)
            }
        } else {
            when (keyCode) {
                in (2..11) -> current = current.plus(typedChar)

                12 -> if (current.isEmpty()) current = current.plus("-")

                14 -> if (current.isNotEmpty()) current = current.dropLast(1)

                28 -> saveCurrent(listening)
            }
        }
    }

    private fun saveCurrent(at: Int) {
        if (current.isBlank()) {
            listening = 0
            return
        }
        try {
            val toSet = if (at == 4) current else current.toInt()

            when (at) {
                1 -> waypoint.x = toSet as Int
                2 -> waypoint.y = toSet as Int
                3 -> waypoint.z = toSet as Int
                4 -> waypoint.name = "§f${toSet as String}"
            }
        } catch (e: Exception) {
            listening = 0
            current = ""
            return
        }
        listening = 0
        current = ""
    }

    private inline val xRender get() = 425 - w1 - w2 - w3 - 39
    private inline val yRender get() = 425 - w1 - w2 - 26
    private inline val zRender get() = 425 - w1 - 13
}