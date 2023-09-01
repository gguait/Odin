package me.odin.ui.waypoint.elements

import cc.polyfrost.oneconfig.renderer.font.Fonts
import cc.polyfrost.oneconfig.utils.dsl.*
import me.odin.ui.waypoint.WaypointGUI
import me.odin.utils.render.gui.GuiUtils.nanoVG
import me.odin.utils.render.gui.GuiUtils.scissor
import me.odin.utils.render.gui.MouseHandler
import java.awt.Color

class AreaButton(
    val area: String,
    private val mouseHandler: MouseHandler
) {

    private var x = 0f
    private var y = 0f
    var width = 0f

    fun draw(vg: VG): Float {
        vg.nanoVG {
            width = getTextWidth(area, 10f, Fonts.REGULAR)
            scissor(0f, 25f, 480f, 25f) {
                if (area == WaypointGUI.displayArea) drawRoundedRect(x - 3, y - 10, width + 7, 18f, 5f, Color(32, 32, 32).rgb)
                drawText(area, x, y, Color.WHITE.rgb, 10, Fonts.REGULAR)
                drawLine(x - 5, y - 7, x - 5, y + 7, 0.7, Color.WHITE.rgb)
            }
        }
        return width + 10
    }

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun mouseClicked(): Boolean = mouseHandler.isAreaHovered(x - 3, y - 10, width + 7, 18f)
}