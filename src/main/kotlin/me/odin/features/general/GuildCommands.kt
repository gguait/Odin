package me.odin.features.general

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.utils.skyblock.ChatUtils
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object GuildCommands {
    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun guild(event: ClientChatReceivedEvent) {
        if (!config.guildCommands) return

        val message = stripControlCodes(event.message.unformattedText)
        val match = Regex("Guild > (\\[.+])? ?(.+) (\\[.+])?: ?!(.+)").find(message) ?: return

        val ign = match.groups[2]?.value
        val msg = match.groups[4]?.value?.lowercase()
        GlobalScope.launch {
            delay(150)
            ChatUtils.guildCmdsOptions(msg!!, ign!!)
            if (config.guildGM && mc.thePlayer.name !== ign) ChatUtils.autoGM(msg, ign)
        }

    }
}