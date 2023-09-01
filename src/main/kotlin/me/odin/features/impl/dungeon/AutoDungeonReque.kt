package me.odin.features.impl.dungeon

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.scope
import me.odin.events.impl.ChatPacketEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.NumberSetting
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object AutoDungeonReque : Module(
    name = "Auto Dungeon Requeue",
    description = "Automatically starts a new dungeon at the end of a dungeon.",
    category = Category.DUNGEON,
    tag = TagType.NEW
) {
    private val delay: Int by NumberSetting("Delay", 10, 0, 30, 1)

    @SubscribeEvent
    fun onChat(event: ChatPacketEvent) {
        if (!DungeonUtils.inDungeons || event.message != "                             > EXTRA STATS <") return
        scope.launch {
            delay(delay * 1000L)
            ChatUtils.sendCommand("instancerequeue")
        }
    }
}