package me.odin.features.impl.skyblock

import me.odin.Odin.Companion.mc
import me.odin.features.Category
import me.odin.features.Module
import net.minecraft.client.settings.KeyBinding.setKeyBindState
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object AutoSprint : Module(
    "Auto Sprint",
    description = "Automatically makes you sprint!",
    category = Category.SKYBLOCK
) {
    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        setKeyBindState(mc.gameSettings.keyBindSprint.keyCode, true)
    }
}