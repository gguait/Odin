package me.odin.features.impl.render

import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import me.odin.Odin
import me.odin.Odin.Companion.display
import me.odin.Odin.Companion.mc
import me.odin.Odin.Companion.scope
import me.odin.config.Config
import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.AlwaysActive
import me.odin.features.settings.impl.*
import me.odin.ui.clickgui.ClickGUI
import me.odin.ui.hud.EditHUDGui
import me.odin.utils.AsyncUtils
import me.odin.utils.WebUtils
import me.odin.utils.render.Color
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.skyblock.LocationUtils
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard

@AlwaysActive
object ClickGUIModule: Module(
    "ClickGUI",
    Keyboard.KEY_RSHIFT,
    category = Category.RENDER
) {
    val blur: Boolean by BooleanSetting("Blur", false, description = "Toggles the background blur for the gui.")
    val enableNotification: Boolean by BooleanSetting("Enable notifications", false, description = "Shows you a notification in chat when you toggle an option with a keybind")
    val color: Color by ColorSetting("GUI Color", Color(50, 150, 220), allowAlpha = false, description = "Color theme in the gui.")
    val switchType: Boolean by DualSetting("Switch Type", "Checkbox", "Switch")

    val action: () -> Unit by ActionSetting("Open Example Hud") {
        display = EditHUDGui
    }

    private var hasJoined: Boolean by BooleanSetting("First join", false, hidden = true)
    var lastSeenVersion: String by StringSetting("Last seen version", "1.0.0", hidden = true)
    var firstTimeOnVersion = false

    val panelX = mutableMapOf<Category, NumberSetting<Float>>()
    val panelY = mutableMapOf<Category, NumberSetting<Float>>()
    val panelExtended = mutableMapOf<Category, BooleanSetting>()

    init {
        execute(250) {
            if (hasJoined) destroyExecutor()
            if (!LocationUtils.inSkyblock) return@execute
            hasJoined = true
            Config.saveConfig()

            modMessage("""
            ${ChatUtils.getChatBreak()}
            §d§kodinOnTopWeLoveodinLiterallyTheBestMod
            
            §7Thanks for installing §3Odin§bClient ${Odin.VERSION}§7!

             §eUse §d§l/od §r§eto access GUI settings.
             §eUse §d§l/od help §r§efor all of of the commands.
             
             §eJoin the discord for support and suggestions.
        """.trimIndent(), false)
            mc.thePlayer.addChatMessage(
                ChatComponentText(" §9https://discord.gg/2nCbC9hkxT")
                .setChatStyle(ChatUtils.createClickStyle(ClickEvent.Action.OPEN_URL, "https://discord.gg/2nCbC9hkxT"))
            )

            modMessage("""
            
            §d§kodinOnTopWeLoveodinLiterallyTheBestMod
            ${ChatUtils.getChatBreak()}
            
        """.trimIndent(), false)
            val uniqueUserWebhook = WebUtils.fetchURLData("https://pastebin.com/raw/0JjdTXLK")
            WebUtils.sendDiscordWebhook(uniqueUserWebhook, mc.thePlayer.name, "${Odin.NAME} ${Odin.VERSION}", 0)

        }

        resetPositions()
    }

    private var hasSentUpdateMessage = false
    private var hasSentWebhook = false

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) = scope.launch {
        if (!hasSentWebhook) {
            hasSentWebhook = true

            val def = AsyncUtils.waitUntilPlayer()
            try { def.await() } catch (e: Exception) { return@launch }

            val userWebhook = WebUtils.fetchURLData("https://pastebin.com/raw/2SY0LKJX")
            WebUtils.sendDiscordWebhook(userWebhook, mc.thePlayer.name, "${Odin.NAME} ${Odin.VERSION}", 0)
        }

        if (hasSentUpdateMessage) return@launch

        val newestVersion = try {
            Json.parseToJsonElement(WebUtils.fetchURLData("https://api.github.com/repos/odtheking/odin/releases/latest"))
        } catch (e: Exception) { return@launch }

        val link = newestVersion.jsonObject["html_url"].toString().replace("\"", "")
        val tag = newestVersion.jsonObject["tag_name"].toString().replace("\"", "")

        if (isSecondNewer(tag)) {
            hasSentUpdateMessage = true

            val def = AsyncUtils.waitUntilPlayer()
            try { def.await() } catch (e: Exception) { return@launch }

            mc.thePlayer.addChatMessage(
                ChatComponentText("§3Odin§bClient §8»§r §7Update available! §r${newestVersion.jsonObject["tag_name"].toString()} §e$link")
                    .setChatStyle(ChatUtils.createClickStyle(ClickEvent.Action.OPEN_URL, link))
            )
        }
    }

    private fun isSecondNewer(second: String): Boolean {
        val (major, minor, patch) = Odin.VERSION.split(".").map { it.toInt() }
        val (major2, minor2, patch2) = second.split(".").map { it.toInt() }
        return when {
            major > major2 -> false
            major < major2 -> true
            minor > minor2 -> false
            minor < minor2 -> true
            patch > patch2 -> false
            patch < patch2 -> true
            else -> false // equal, or something went wrong, either way it's best to assume it's false.
        }
    }

    fun resetPositions() {
        Category.entries.forEach {
            val incr = 10f + 260f * it.ordinal
            panelX.getOrPut(it) { +NumberSetting(it.name + ",x", default = incr, hidden = true) }.value = incr
            panelY.getOrPut(it) { +NumberSetting(it.name + ",y", default = 10f, hidden = true) }.value = 10f
            panelExtended.getOrPut(it) { +BooleanSetting(it.name + ",extended", default = true, hidden = true) }.enabled = true
        }
    }

    override fun onKeybind() {
        this.toggle()
    }

    override fun onEnable() {
        display = ClickGUI
        super.onEnable()
        toggle()
    }
}
