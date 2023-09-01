package me.odin.features.impl.floor7

import me.odin.events.impl.ChatPacketEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.SelectorSetting
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object RelicAnnouncer : Module(
    name = "Relic Announcer",
    description = "Automatically announce your relic to the rest of the party",
    category = Category.FLOOR7,
    tag = TagType.NEW
) {
    private val colors = arrayListOf("Green", "Purple", "Blue", "Orange", "Red")
    private val selected: Int by SelectorSetting("Color", "Green", colors)

    @SubscribeEvent
    fun onChatReceived(event: ChatPacketEvent) {
        if (!DungeonUtils.inDungeons) return
        if (event.message !== "[BOSS] Necron: All this, for nothing...") return
        ChatUtils.partyMessage("${colors[selected]} Relic")
    }
}