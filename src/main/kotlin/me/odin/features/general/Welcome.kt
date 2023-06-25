package me.odin.features.general

import cc.polyfrost.oneconfig.libs.universal.UChat
import me.odin.Odin
import me.odin.Odin.Companion.mc
import me.odin.Odin.Companion.miscConfig
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.LocationUtils
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent


object Welcome {


    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (miscConfig.hasJoined || event.phase != TickEvent.Phase.START || !LocationUtils.inSkyblock) return
        miscConfig.hasJoined = true
        miscConfig.saveAllConfigs()
        UChat.chat("""
            ${ChatUtils.getChatBreak().dropLast(1)}
            §d§kOdinOnTopWeLoveOdinIsLiteralyTheBestNoDoubt
            
            §7Thanks for installing §3Odin §7!
            §7You are currently on version ${Odin.VERSION}.
            
            §eUse §d§l/od §r§eto access GUI settings.
            §eUse §d§l/od help §r§efor all of of the commands.
             
             §eJoin the discord for support and suggestions.
        """.trimIndent())
        mc.thePlayer.addChatMessage(ChatComponentText(" §9https://discord.gg/2nCbC9hkxT")
            .setChatStyle(createClickStyle(ClickEvent.Action.OPEN_URL, "https://discord.gg/2nCbC9hkxT")))

        UChat.chat("""
            
            §d§kOdinOnTopWeLoveOdinIsLiteralyTheBestNoDoubt
            ${ChatUtils.getChatBreak()}
        """.trimIndent())

    }

    private fun createClickStyle(action: ClickEvent.Action?, value: String): ChatStyle {
        val style = ChatStyle()
        style.setChatClickEvent(ClickEvent(action, value))
        style.setChatHoverEvent(
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ChatComponentText(EnumChatFormatting.YELLOW.toString() + value)
            )
        )
        return style
    }
}