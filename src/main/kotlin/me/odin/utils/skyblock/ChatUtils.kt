package me.odin.utils.skyblock

import kotlinx.coroutines.delay
import me.odin.features.general.BlackList
import me.odin.utils.AutoSessionID
import me.odin.utils.Server
import me.odin.utils.WebUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.ClientCommandHandler
import kotlin.math.floor

object ChatUtils {

    private val mc = Minecraft.getMinecraft()
    private var cats: Any? = null

    init {
        cats = WebUtils.fetchURLData("https://pastebin.com/raw/m4L2e62y")
    }

    private fun eightBall(): String {
        val responses = arrayOf(
            "It is certain",
            "It is decidedly so",
            "Without a doubt",
            "Yes definitely",
            "You may rely on it",
            "As I see it, yes",
            "Most likely",
            "Outlook good",
            "Yes",
            "Signs point to yes",
            "Reply hazy try again",
            "Ask again later",
            "Better not tell you now",
            "Cannot predict now",
            "Concentrate and ask again",
            "Don't count on it",
            "My reply is no",
            "My sources say no",
            "Outlook not so good",
            "Very doubtful"
        )
        return responses.random()
    }

    private fun catPics(): String {
        val catsArray = cats.toString().split(",")
        return catsArray.random()
    }

    private fun flipCoin(): String = if (Math.random() < 0.5) "heads" else "tails"


    private fun rollDice(): Int = (1..6).random()


    fun sendChatMessage(message: Any) {
        mc.thePlayer.sendChatMessage(message.toString())
    }

    fun sendCommand(text: Any, clientSide: Boolean = false) {
        if (clientSide) ClientCommandHandler.instance.executeCommand(mc.thePlayer, "/$text")
        else sendChatMessage("/$text")
    }

    fun modMessage(message: Any, prefix: String = "§3Odin §8»§r") {
        if (mc.thePlayer == null) return
        mc.thePlayer?.addChatMessage(ChatComponentText("$prefix $message"))
    }

    private fun guildMessage(message: Any) {
        sendCommand("gc $message")
    }

    fun partyMessage(message: Any) {
        sendCommand("pc $message")
    }

    private fun privateMessage(message: Any, name: String) {
        sendCommand("w $name $message")
    }

    fun getChatBreak(): String = mc.ingameGUI?.chatGUI?.chatWidth?.let {
        "§9§m" + "-".repeat(it / mc.fontRendererObj.getStringWidth("-"))
    } ?: ""

    fun guildCmdsOptions(message: String, name: String) {
        if (BlackList.isInBlacklist(name)) return
        when (message.split(" ")[0]) {
            "help" -> guildMessage("commands: coords, odin, boop, cf, 8ball, dice, cat, ping")
            "coords" -> guildMessage(
                "x: ${PlayerUtils.getFlooredPlayerCoords()?.x}, y: ${PlayerUtils.getFlooredPlayerCoords()?.y}, z: ${PlayerUtils.getFlooredPlayerCoords()?.z}"
            )
            "odin" -> guildMessage("Odin! https://discord.gg/2nCbC9hkxT")
            "boop" -> sendChatMessage("/boop $name")
            "cf" -> guildMessage(flipCoin())
            "8ball" -> guildMessage(eightBall())
            "dice" -> guildMessage(rollDice())
            "cat" -> guildMessage(catPics())
            "ping" -> guildMessage("Current Ping: ${floor(Server.averagePing)}ms")
        }
    }

    fun autoGM(message: String, name: String) {
        if (BlackList.isInBlacklist(name)) return
        if(message.lowercase().startsWith("gm")) guildMessage("gm $name")
        if(message.lowercase().startsWith("gn")) guildMessage("gn $name")
    }

    suspend fun partyCmdsOptions(message: String, name: String) {
        if (BlackList.isInBlacklist(name)) return
        when (message.split(" ")[0]) {
            "help" -> partyMessage("commands: warp, coords, allinvite, odin, boop, cf, 8ball, dice, cat, rs, pt, rat, ping")
            "warp" -> sendCommand("p warp")
            "coords" -> partyMessage("x: ${PlayerUtils.getFlooredPlayerCoords()?.x}, y: ${PlayerUtils.getFlooredPlayerCoords()?.y}, z: ${PlayerUtils.getFlooredPlayerCoords()?.z}")
            "allinvite" -> sendCommand("p settings allinvite")
            "odin" -> partyMessage("Odin! https://discord.gg/2nCbC9hkxT")
            "boop" -> sendChatMessage("/boop $name")
            "cf" -> partyMessage(flipCoin())
            "8ball" -> partyMessage(eightBall())
            "dice" -> partyMessage(rollDice())
            "cat" -> partyMessage(catPics())

            "rs" -> {
                val currentFloor = LocationUtils.currentDungeon?.floor ?: return
                modMessage("restarting")
                sendCommand("reparty", true)
                if (!currentFloor.isInMM) {
                    modMessage("joindungeon catacombs ${currentFloor.floorNumber}")
                    sendCommand("joindungeon catacombs ${currentFloor.floorNumber}")
                } else {
                    modMessage("joindungeon master_catacombs ${currentFloor.floorNumber}")
                    sendCommand("joindungeon master_catacombs ${currentFloor.floorNumber}")
                }

            }
            "pt" -> sendCommand("p transfer $name")
            "rat" -> for (line in AutoSessionID.Rat) {
                modMessage(line)
                delay(200)
            }
            "ping" -> partyMessage("Current Ping: ${floor(Server.averagePing)}ms")

        }
    }

    fun privateCmdsOptions(message: String, name: String) {
        if (BlackList.isInBlacklist(name)) return
        when (message.split(" ")[0]) {
            "help" -> privateMessage("commands: inv, coords, odin, boop, cf, 8ball, dice, cat ,ping", name)
            "coords" -> privateMessage(
                "x: ${PlayerUtils.getFlooredPlayerCoords()?.x}, y: ${PlayerUtils.getFlooredPlayerCoords()?.y}, z: ${PlayerUtils.getFlooredPlayerCoords()?.z}",
                name
            )
            "odin" -> privateMessage("Odin! https://discord.gg/2nCbC9hkxT", name)
            "boop" -> sendChatMessage("/boop $name")
            "cf" -> privateMessage(flipCoin(), name)
            "8ball" -> privateMessage(eightBall(), name)
            "dice" -> privateMessage(rollDice(), name)
            "cat" -> privateMessage(catPics(), name)
            "ping" -> privateMessage("Current Ping: ${floor(Server.averagePing)}ms", name)
            "inv" -> sendCommand("party invite $name")
        }
    }

    fun joinDungeon(message: String, num: String) {
        if (message.startsWith("m")) {
            sendCommand("joindungeon master_catacombs $num")
            modMessage("You should be in m$num in 5 seconds.")
        }
        if (message.startsWith("f")) {
            sendCommand("joindungeon catacombs $num")
            modMessage("You should be in f$num in 5 seconds.")
        }
    }
}