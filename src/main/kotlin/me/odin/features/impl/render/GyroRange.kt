package me.odin.features.impl.render

import me.odin.Odin.Companion.mc
import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.ColorSetting
import me.odin.features.settings.impl.NumberSetting
import me.odin.utils.render.Color
import me.odin.utils.render.world.RenderUtils
import me.odin.utils.skyblock.ItemUtils.heldItem
import me.odin.utils.skyblock.ItemUtils.itemID
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object GyroRange : Module(
    "Gyro Range",
    description = "Renders a helpful circle to show the range of the Gyrokinetic Wand.",
    category = Category.SKYBLOCK
) {
    private val color: Color by ColorSetting("Color", Color(192, 64, 192, 0.5f), allowAlpha = true)
    private val thickness: Float by NumberSetting("Thickness", 1f, 0, 10, 0.25)
    private val steps: Int by NumberSetting("Smoothness", 40, 20, 80, 1)

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (heldItem?.itemID != "GYROKINETIC_WAND") return
        val pos = mc.thePlayer.rayTrace(25.0, event.partialTicks)?.blockPos ?: return
        val block = mc.theWorld?.getBlockState(pos)?.block ?: return
        if (block.isAir(mc.theWorld, pos)) return

        RenderUtils.drawCylinder(
            Vec3(pos).addVector(0.5, 1.0, 0.5),
            10f, 10f - thickness, 0.2f,
            steps, 1,
            0f, 90f, 90f,
            color.r / 255f,
            color.g / 255f,
            color.b / 255f,
            color.alpha
        )
    }
}
