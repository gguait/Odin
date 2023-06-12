package me.odin.hud

import cc.polyfrost.oneconfig.hud.TextHud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import me.odin.features.general.DeployableTimer
import me.odin.utils.render.RenderUtils
import net.minecraft.util.ResourceLocation

class DeployableHud: TextHud(false) {
    private val firework: ResourceLocation = ResourceLocation("odin", "firework.png")

    override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
        for (i in lines.indices) {
            drawLine(lines[i], x, y + i * 12 * scale, scale)
        }
        if (example) {
            RenderUtils.renderImage(firework, x.toDouble() - scale * 25, y.toDouble(), 20 * scale)
        } else if (DeployableTimer.lines.third != null) {
            RenderUtils.renderImage(DeployableTimer.lines.third!!, x.toDouble() - scale * 25, y.toDouble(), 20 * scale)
        }
    }

    override fun getLines(lines: MutableList<String>?, example: Boolean) {
        if (example) {
            lines?.add(0, "§5§lSOS Flare")
            lines?.add(1, "§e179s")
        } else if (DeployableTimer.lines.first != null) {
            lines?.add(0, DeployableTimer.lines.first!!)
            lines?.add(1, DeployableTimer.lines.second!!)
        }
    }
}