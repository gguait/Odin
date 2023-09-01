package me.odin.events

import kotlinx.coroutines.launch
import me.odin.Odin.Companion.mc
import me.odin.Odin.Companion.scope
import me.odin.events.impl.*
import me.odin.utils.AsyncUtils
import me.odin.utils.ServerUtils
import me.odin.utils.Utils.noControlCodes
import me.odin.utils.clock.Clock
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.network.play.server.S02PacketChat
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object EventDispatcher {

    private var tickRamp = 0

    /** Used to make code simpler. */
    fun post(event: Event) {
        MinecraftForge.EVENT_BUS.post(event)
    }

    /**
     * Dispatches [ChatPacketEvent].
     */
    @SubscribeEvent
    fun onPacket(event: ReceivePacketEvent) {
        if (event.packet is S02PacketChat) {
            post(ChatPacketEvent(event.packet.chatComponent.unformattedText.noControlCodes))
        }
    }

    /**
     * Dispatches [ClientSecondEvent]
     */
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        tickRamp++

        if (tickRamp % 20 == 0) {
            if (mc.thePlayer != null) post(ClientSecondEvent())
            tickRamp = 0
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        tickRamp = 18
    }

    // use this. alot more readable and cleaner code. Should be barely less performant cuz everything is inlined.
    private val nextTime = Clock()

    /**
     * Dispatches [ServerTickEvent]
     */
    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (nextTime.hasTimePassed((1000L / ServerUtils.averageTps).toLong(), setTime = true)) {
            post(ServerTickEvent())
        }
    }

    /**
     * Dispatches [GuiLoadedEvent]
     */
    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) = scope.launch {
        if (event.gui !is GuiChest) return@launch

        val container = (event.gui as GuiChest).inventorySlots

        if (container !is ContainerChest) return@launch
        val chestName = container.lowerChestInventory.displayName.unformattedText

        val deferred = AsyncUtils.waitUntilLastItem(container)
        try { deferred.await() } catch (e: Exception) { return@launch } // Wait until the last item in the chest isn't null

        MinecraftForge.EVENT_BUS.post(GuiLoadedEvent(chestName, container))
    }
}