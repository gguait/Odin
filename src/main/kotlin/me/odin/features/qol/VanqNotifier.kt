package me.odin.features.qol

import me.odin.Odin.Companion.config
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.PlayerUtils
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object VanqNotifier {
    @SubscribeEvent
    fun onClientChatReceived(event: ClientChatReceivedEvent) {
        if(!config.vanqNotifier) return
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (message !== "A Vanquisher is spawning nearby!") return
        ChatUtils.modMessage("Vanquisher has spawned!")
        PlayerUtils.alert("Vanquisher has spawned!")
        if (config.vanqNotifierAC) ChatUtils.sendChatMessage("Vanquisher spawned at: x: ${PlayerUtils.getFlooredPlayerCoords()?.x}, y: ${PlayerUtils.getFlooredPlayerCoords()?.y}, z: ${PlayerUtils.getFlooredPlayerCoords()?.z}")
        if (config.vanqNotifierPC) ChatUtils.partyMessage("Vanquisher spawned at: x: ${PlayerUtils.getFlooredPlayerCoords()?.x}, y: ${PlayerUtils.getFlooredPlayerCoords()?.y}, z: ${PlayerUtils.getFlooredPlayerCoords()?.z}")

    }
}