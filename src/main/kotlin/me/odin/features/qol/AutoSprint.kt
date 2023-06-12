package me.odin.features.qol

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import net.minecraft.client.settings.KeyBinding.setKeyBindState
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object AutoSprint {
    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!config.autoSprint) return
        setKeyBindState(mc.gameSettings.keyBindSprint.keyCode, true)
    }
}