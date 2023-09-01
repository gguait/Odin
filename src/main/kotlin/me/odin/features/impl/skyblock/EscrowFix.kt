package me.odin.features.impl.skyblock

import me.odin.events.impl.ChatPacketEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.utils.skyblock.ChatUtils.sendCommand
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EscrowFix : Module(
    name = "Escrow Fix",
    description = "Automatically reopens the ah/bz when it gets closed by escrow",
    category = Category.SKYBLOCK,
    tag = TagType.NEW
) {
    //need to get bazzar actual message still
    private val messages = hashMapOf(
        "Visit the Bazaar to collect your item!" to "bz",
        "(AUCTION_EXPIRED_OR_NOT_FOUND)" to "ah",
    )

    @SubscribeEvent
    fun onClientChatReceived(event: ChatPacketEvent) {
        messages[event.message]?.let { sendCommand(it) }
    }
}