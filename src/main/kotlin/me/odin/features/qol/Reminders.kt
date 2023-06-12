package me.odin.features.qol

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.PlayerUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object Reminders {

    private var firstLazer = false
    private var timeInDungeon: Long = 0
    private var notified = false
    private var playerReady = false
    private var spiritProc = 0L
    private var bonzoProc = 0L
    private const val spiritCooldown = 30000
    private const val bonzoCooldown = 180000

    private val alertMap = mapOf(
        "[BOSS] Wither King: You.. again?" to "&3Swap to edrag!",
        "[BOSS] Maxor: YOU TRICKED ME!" to "&3Use ult!",
        "[BOSS] Maxor: THAT BEAM! IT HURTS! IT HURTS!!" to "&3Use ult!",
        "[BOSS] Goldor: You have done it, you destroyed the factory…" to "&3Use ult!",
        "[BOSS] Sadan: My giants! Unleashed!" to "&3Use ult!"
        // Add more pairs here as needed
    )

    @SubscribeEvent
    fun onClientChatReceived(event: ClientChatReceivedEvent) {
        if (!DungeonUtils.inDungeons) return

        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (config.maskAlert) {
            if (Regex("^(Second Wind Activated!)? ?Your (.+) saved your life!\$").matches(message)) {
                PlayerUtils.alert("Mask used!")
                ChatUtils.modMessage("Mask used!")
            }
        }
        if (message in alertMap) {
            if (message.startsWith("[BOSS] Maxor:") && firstLazer) return
            if(message.startsWith("[BOSS] Wither King") && !config.dragonReminder) return
            if(message.startsWith("[BOSS] Maxor") && !config.ultReminder) return
            if(message.startsWith("[BOSS] Sadan") && !config.ultReminder) return
            if(message.startsWith("[BOSS] Goldor") && !config.ultReminder) return

            val alert = alertMap[message]
            PlayerUtils.alert(alert!!)
            ChatUtils.modMessage(alert)

            if (message.startsWith("[BOSS] Maxor:")) {
                firstLazer = true
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        timeInDungeon = System.currentTimeMillis()
        notified = false
        playerReady = false
        firstLazer = false
        spiritProc = 0
        bonzoProc = 0
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.readyReminder || !DungeonUtils.inDungeons || playerReady || notified || System.currentTimeMillis() - timeInDungeon <= 7000) return
        PlayerUtils.alert("&3Ready up!")
        ChatUtils.modMessage("Ready up!")
        notified = true
    }

    @SubscribeEvent
    fun playerReady(event: ClientChatReceivedEvent) {
        val message = event.message.unformattedText
        if (message == "${mc.thePlayer.name} is now ready!") {
            playerReady = true
            mc.thePlayer.closeScreen()
        }
    }
}


