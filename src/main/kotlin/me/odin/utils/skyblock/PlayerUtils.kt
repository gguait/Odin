package me.odin.utils.skyblock

import me.odin.Odin.Companion.mc
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import kotlin.math.floor

object PlayerUtils {

    fun getFlooredPlayerCoords(): Vec3i? =
        if (mc.thePlayer == null) null
        else mc.thePlayer.positionVector.floored()
        //else Vec3i(floor(mc.thePlayer.posX), floor(mc.thePlayer.posY), floor(mc.thePlayer.posZ))

    fun Vec3.floored() = Vec3i(floor(this.xCoord), floor(this.yCoord), floor(this.zCoord))

    fun removeSymbols(input: String): String = input.replace(Regex("[^a-zA-Z0-9 ]"), "")


    private fun playSound(name: String, volume: Float, pitch: Float) = mc.thePlayer.playSound(name, volume, pitch)

    fun alert(title: String, playSound: Boolean = true) {
        if (playSound) playSound("note.pling", 100F, 1F)
        mc.ingameGUI.displayTitle(title, "", 10, 150, 10)
    }


    val posX get() = mc.thePlayer.posX
    val posY get() = mc.thePlayer.posY
    val posZ get() = mc.thePlayer.posZ
}