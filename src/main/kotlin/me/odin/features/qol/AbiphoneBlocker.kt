package me.odin.features.qol

import kotlinx.coroutines.*
import me.odin.Odin.Companion.config
import me.odin.utils.skyblock.ChatUtils.modMessage
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object AbiphoneBlocker {
    private var blocking = false

    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun onNPC(event: ClientChatReceivedEvent) {
        if (!config.abiphoneBlocker) return
        val message = stripControlCodes(event.message.unformattedText)
        if (message.contains("Elle")) return
        val match = Regex("✆ (.+) ✆").find(message) ?: return
        val npc = match.groups[1]?.value
        modMessage("§7Ghosted a call from §6$npc")
        blocking = true
        GlobalScope.launch {
            delay(5000)
            blocking = false
        }
        event.isCanceled = true
    }

    @SubscribeEvent
    fun onCall(event: ClientChatReceivedEvent) {
        if (!config.abiphoneBlocker || !blocking) return
        val message = stripControlCodes(event.message.unformattedText)
        Regex("✆ (.+) [PICK UP]").find(message) ?: return
        event.isCanceled = true
    }

    @SubscribeEvent(receiveCanceled = false)
    fun onSound(event: PlaySoundEvent) {
        if (!config.abiphoneBlocker || !blocking || event.name != "note.pling") return
        event.result = null
    }
}