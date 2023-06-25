package me.odin.features.general

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.config
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.ChatUtils.dtToggle
import me.odin.utils.skyblock.PlayerUtils
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PartyCommands {


    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun party(event: ClientChatReceivedEvent) {
        if (!config.partyCommands) return

        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        val match = Regex("Party > (\\[.+\\])? ?(.+): !(.+)").find(message) ?: return

        val ign = match.groups[2]?.value
        val msg = match.groups[3]?.value?.lowercase()

        GlobalScope.launch{
            delay(150)
            ChatUtils.partyCmdsOptions(msg!!, ign!!)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun dt(event: ClientChatReceivedEvent) {
        if (!config.partyCommands) return
        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        if (!message.contains("EXTRA STATS") || ChatUtils.dtPlayer == null) return
        GlobalScope.launch{
            delay(2500)
            PlayerUtils.alert("§c${ChatUtils.dtPlayer} needs downtime")
            ChatUtils.partyMessage("${ChatUtils.dtPlayer} needs downtime")
            ChatUtils.dtPlayer = null
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun private(event: ClientChatReceivedEvent) {
        if (!config.partyCommands) return

        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        val match = Regex("From (\\[.+\\])? ?(.+): !(.+)").find(message) ?: return

        val ign = match.groups[2]?.value
        val msg = match.groups[3]?.value?.lowercase()
        GlobalScope.launch {
            delay(150)
            ChatUtils.privateCmdsOptions(msg!!, ign!!)
        }
    }
    @SubscribeEvent
    fun joinDungeon(event: ClientChatReceivedEvent) {
        if (!config.partyCommands) return

        val message = StringUtils.stripControlCodes(event.message.unformattedText)
        val match = Regex("(Party >) (\\[.+\\])? ?(.+): !(.+) (.+)").find(message) ?: return

        val msg = match.groups[3]?.value?.lowercase()
        val num = match.groups[4]?.value

        ChatUtils.joinDungeon(msg!!, num!!)
    }
}
