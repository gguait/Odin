package me.odin.utils.gui

import cc.polyfrost.oneconfig.renderer.font.Font
import cc.polyfrost.oneconfig.renderer.font.Fonts
import cc.polyfrost.oneconfig.renderer.scissor.ScissorHelper
import cc.polyfrost.oneconfig.utils.dsl.*
import me.odin.ui.waypoint.WaypointGUI
import net.minecraft.client.gui.ScaledResolution

/**
 * This is here for if you want to make more guis
 */
object GuiUtils {

    val scaledWidth get() =
        ScaledResolution(WaypointGUI.mc).scaledWidth

    val scaledHeight get() =
        ScaledResolution(WaypointGUI.mc).scaledHeight

    val scaleFactor: Float
        get() {
            val scale = (scaledWidth / 960f).coerceAtMost(scaledHeight / 540f)
            return (scale.coerceAtLeast(1f / 1280f).coerceAtLeast(1f / 800f)).coerceIn(0.05f, 1f)
        }

    fun VG.translateWithMouse(mouseHandler: MouseHandler, x: Float, y: Float) { // bad name refactor later same with scale func
        this.translate(x, y)
        mouseHandler.translate(x, y)
    }

    fun VG.scaleWithMouse(mouseHandler: MouseHandler, x: Float, y: Float) {
        this.scale(x, y)
        mouseHandler.scale(x, y)
    }

    inline fun VG.scissor(x: Float, y: Float, width: Float, height: Float, action: () -> Unit) {
        ScissorHelper.INSTANCE.scissor(this.instance, x, y, width, height)
        action.invoke()
        ScissorHelper.INSTANCE.clearScissors(this.instance)
    }

    fun VG.drawCustomCenteredText(string: String, x: Number, y: Number, color: Int, size: Number = 16f, font: Font = Fonts.REGULAR) {
        val textWidth = (x.toFloat() - this.getTextWidth(string, size, font) / 2f)
        this.drawText(string, textWidth, y, color, size, font)
    }
}