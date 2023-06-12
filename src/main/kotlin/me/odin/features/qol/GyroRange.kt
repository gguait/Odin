package me.odin.features.qol


import cc.polyfrost.oneconfig.libs.universal.ChatColor.Companion.stripControlCodes
import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.utils.render.RenderUtils
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object GyroRange {

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (mc.thePlayer?.heldItem?.displayName == null) return
        if (
            !config.gyroRange ||
            !stripControlCodes(mc.thePlayer.heldItem.displayName)?.contains("Gyrokinetic Wand")!!
        ) return
        val pos = mc.thePlayer.rayTrace(25.0, event.partialTicks)?.blockPos ?: return
        val block = mc.theWorld?.getBlockState(pos)?.block ?: return
        if (block.isAir(mc.theWorld, pos)) return
        RenderUtils.drawCylinder(
            Vec3(pos).addVector(0.5, 1.0, 0.5),
            10f,
            10f - config.gyroThickness,
            0.2f,
            config.gyroSteps.toInt(),
            1,
            0f,
            90f,
            90f,
            config.gyroColor.red / 255.0,
            config.gyroColor.green / 255.0,
            config.gyroColor.blue / 255.0,
            config.gyroColor.alpha / 255.0,
            phase = false,
            linemode = false
        )
    }
}