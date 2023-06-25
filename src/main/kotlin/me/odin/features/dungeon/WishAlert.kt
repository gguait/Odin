package me.odin.features.dungeon

import me.odin.Odin.Companion.config
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.PlayerUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import kotlin.math.floor


object WishAlert {

    private var canWish = true

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if ((message.contains("Wish is ready to use!") || message.contains("Your Healer ULTIMATE wish is now available!")) && !DungeonUtils.inBoss && !DungeonUtils.isGhost)
            canWish = true
        else if (DungeonUtils.inBoss && canWish && !DungeonUtils.isGhost)
            canWish = false
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (!config.wishAlert || DungeonUtils.inBoss || !DungeonUtils.inDungeons || !canWish) return
        DungeonUtils.teammates.forEach { entityPlayer ->
            val currentHp = entityPlayer.first.health
            val healthPercent = 40 * (config.healthPrecentage / 100)
            println("Current HP: $currentHp, Health Percent: $healthPercent")
            println("is ghost ${DungeonUtils.isGhost}")
            if (currentHp < 40 * (config.healthPrecentage / 100) && !DungeonUtils.isGhost) {
                ChatUtils.modMessage("§7${entityPlayer.first.name}§a is at less than §c${floor(config.healthPrecentage)}% §aHP!")
                PlayerUtils.alert("USE WISH")
                canWish = false
            }
        }
    }
}