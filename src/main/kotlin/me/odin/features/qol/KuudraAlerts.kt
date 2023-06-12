package me.odin.features.qol

import me.odin.Odin.Companion.config
import me.odin.utils.skyblock.PlayerUtils
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object KuudraAlerts {
    private val map: Map<String, String> = mapOf(
        "WARNING: You do not have a key for this tier in your inventory, you will not be able to claim rewards." to "§l§4NO KUUDRA KEY!",
        "[NPC] Elle: Okay adventurers, I will go and fish up Kuudra!" to "§l§4BUY UPGRADE ROUTE!",
        "[NPC] Elle: Not again!" to "§lPICKUP SUPPLY!",
        "[NPC] Elle: It's time to build the Ballista again! Cover me!" to "§l§4START BUILDING!",
        "Your Fresh Tools Perk bonus doubles your building speed for the next 5 seconds!" to "§l§4EAT FRESH!"
    )

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.kuudraAlerts) return
        val message = stripControlCodes(event.message.unformattedText)
        if (map.containsKey(message)) {
            map[message]?.let { PlayerUtils.alert(it) }
        }
    }

    @SubscribeEvent
    fun onStun(event: ClientChatReceivedEvent) {
        if (!config.kuudraAlerts) return
        val message = stripControlCodes(event.message.unformattedText)
        Regex("(.+) destroyed one of Kuudra's pods!").find(message) ?: return
        PlayerUtils.alert("§l§4KUUDRA STUNNED!")
    }

    @SubscribeEvent
    fun onUnready(event: ClientChatReceivedEvent) {
        if (!config.kuudraAlerts) return
        val message = stripControlCodes(event.message.unformattedText)
        val match = Regex("(.+) is no longer ready!").find(message) ?: return
        val ign = match.groups[1]?.value
        PlayerUtils.alert("§l§4$ign IS NO LONGER READY!")
    }
}