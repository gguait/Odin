package me.odin.features.impl.skyblock

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.scope
import me.odin.events.impl.ChatPacketEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.NumberSetting
import me.odin.utils.skyblock.ChatUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object AutoDungeonReque : Module(
    name = "Auto Leave Limbo",
    description = "Automatically leaves Limbo when you get sent to it.",
    category = Category.SKYBLOCK,
    tag = TagType.NEW
) {
    private val delay: Int by NumberSetting("Delay", 10, 0, 30, 1)

    @SubscribeEvent
    fun onChat(event: ChatPacketEvent) {
        if (event.message != "Oops! You are not on SkyBlock so we couldn't warp you!") return
        scope.launch {
            delay(delay * 1000L)
            ChatUtils.sendCommand("l")
            delay(delay * 1000L)
            ChatUtils.sendCommand("play sb")
        }
    }
}