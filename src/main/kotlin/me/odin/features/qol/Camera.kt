package me.odin.features.qol

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object Camera {
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.frontCamera && mc.gameSettings.thirdPersonView == 2) mc.gameSettings.thirdPersonView = 0
    }
}