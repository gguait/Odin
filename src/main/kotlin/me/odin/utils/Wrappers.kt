package me.odin.utils

import me.odin.Odin
import net.minecraft.client.Minecraft

open class Wrappers {
    companion object {
        val mc: Minecraft = Odin.mc

        val posX
            get() = mc.thePlayer.posX

        val posY
            get() = mc.thePlayer.posY

        val posZ
            get() = mc.thePlayer.posZ

        val cfg
            get() = Odin.config
    }
}