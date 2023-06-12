package me.odin.features.qol

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object NoCursorReset {
    private var lastContainerOpen = 0L
    private var hasBeenNullFor = 0

    @SubscribeEvent
    fun onGuiOpen(e: GuiOpenEvent) {
        val oldGuiScreen = mc.currentScreen
        if (e.gui is GuiChest && (oldGuiScreen is GuiContainer || oldGuiScreen == null))
            lastContainerOpen = System.currentTimeMillis()
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (mc.currentScreen != null) hasBeenNullFor = 0
        else hasBeenNullFor++
    }

    fun shouldHookMouse() = System.currentTimeMillis() - lastContainerOpen < 100 && config.noCursorReset && hasBeenNullFor == 0
}