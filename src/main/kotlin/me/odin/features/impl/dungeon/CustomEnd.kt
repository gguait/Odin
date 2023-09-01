package me.odin.features.impl.dungeon

import me.odin.events.impl.ChatPacketEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils.inDungeons
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object CustomEnd : Module(
    name = "Auto Extra stats",
    description = "Automatically clicks the Extra Stats at the end of a dungeon.",
    category = Category.DUNGEON,
) {

    @SubscribeEvent
    fun onChat(event: ChatPacketEvent) {
        if (!inDungeons) return
        if (event.message == "                             > EXTRA STATS <") {
            ChatUtils.sendCommand("showextrastats")
        }
    }
}