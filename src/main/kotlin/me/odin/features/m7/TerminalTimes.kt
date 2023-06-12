package me.odin.features.m7

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.Odin.Companion.miscConfig
import me.odin.utils.skyblock.ChatUtils.modMessage
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object TerminalTimes {

    private var inTerm = false
    private var timer = 0L
    private var currentTerminal = ""
    private val terminalNames = listOf(
        "Correct all the panes!",
        "Change all to same color!",
        "Click in order!",
        "Click the button on time!",
        "What starts with",
        "Select all the"
    )

    enum class Times (
        val fullName: String,
        var time: Double = 1000.0
    ) {
        Panes("Correct all the panes!"),
        Color("Change all to same color!"),
        Numbers("Click in order!"),
        Melody("Click the button on time!"),
        StartsWith("What starts with"),
        Select("Select all the");
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (inTerm || !config.termTimer) return
        val currentScreen = mc.currentScreen

        if (currentScreen !is GuiChest) return
        val container = currentScreen.inventorySlots
        if (container !is ContainerChest) return
        val chestName = container.lowerChestInventory.displayName.unformattedText ?: return

        terminalNames.forEach { name ->
            if (chestName.startsWith(name)) {
                modMessage("entered $name")
                inTerm = true
                currentTerminal = chestName
                timer = System.currentTimeMillis()
            }
        }
    }

    @SubscribeEvent
    fun onClientChatReceived(event: ClientChatReceivedEvent) {
        if (!config.termTimer) return
        val message = stripControlCodes(event.message.unformattedText)
        val match = Regex("(.+) (?:activated|completed) a terminal! \\(\\d/\\d\\)").find(message) ?: return
        val name = match.groups[1]?.value
        if (name != mc.thePlayer.name) return
        inTerm = false
        val time = (System.currentTimeMillis() - timer) / 1000.0

        modMessage("§6$currentTerminal §ftook §a${time}s")

        for (times in Times.values()) {
            if (times.fullName == currentTerminal && time < times.time) {
                modMessage("§fNew best time for §6${times.fullName} §fis §a${time}s, §fold best time was §a${times.time}s")
                times.time = time
                miscConfig.saveAllConfigs()
                break
            }
        }

        currentTerminal = ""
    }
}
