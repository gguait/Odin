package me.odin.features.impl.skyblock

import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.BooleanSetting
import me.odin.utils.Utils.floor
import me.odin.utils.Utils.noControlCodes
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.PlayerUtils
import me.odin.utils.skyblock.PlayerUtils.posX
import me.odin.utils.skyblock.PlayerUtils.posY
import me.odin.utils.skyblock.PlayerUtils.posZ
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object VanqNotifier : Module(
    "Vanq Notifier",
    category = Category.SKYBLOCK
) {
    private val ac: Boolean by BooleanSetting("All chat")
    private val pc: Boolean by BooleanSetting("Party chat")

    @SubscribeEvent
    fun onClientChatReceived(event: ClientChatReceivedEvent) {
        val message = event.message.unformattedText.noControlCodes
        if (message !== "A Vanquisher is spawning nearby!") return

        ChatUtils.modMessage("Vanquisher has spawned!")
        PlayerUtils.alert("§5Vanquisher has spawned!")

        if (ac) ChatUtils.sendChatMessage("Vanquisher spawned at: x: ${posX.floor()}, y: ${posY.floor()}, z: ${posZ.floor()}")
        if (pc) ChatUtils.partyMessage("Vanquisher spawned at: x: ${posX.floor()}, y: ${posY.floor()}, z: ${posZ.floor()}")
    }
}