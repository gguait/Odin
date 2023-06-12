package me.odin.utils.skyblock

import me.odin.Odin.Companion.mc
import me.odin.events.ClientSecondEvent
import me.odin.utils.skyblock.ScoreboardUtils.cleanSB
import me.odin.utils.skyblock.ScoreboardUtils.sidebarLines
import me.odin.utils.skyblock.dungeon.Dungeon
import me.odin.utils.skyblock.dungeon.DungeonUtils.getPhase
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object LocationUtils {

    private var onHypixel: Boolean = false
    var inSkyblock: Boolean = false

    var currentDungeon: Dungeon? = null
    var currentArea: String? = null

    @SubscribeEvent
    fun onSecond(event: ClientSecondEvent) {
        if (!inSkyblock) {
            inSkyblock = onHypixel && mc.theWorld.scoreboard.getObjectiveInDisplaySlot(1)
                ?.let { cleanSB(it.displayName).contains("SKYBLOCK") } ?: false
        }

        if (currentDungeon == null)
            if (inSkyblock && sidebarLines.any {
                    cleanSB(it).run { (contains("The Catacombs") && !contains("Queue")) || contains("Dungeon Cleared:") }
                }) currentDungeon = Dungeon()

        if (currentArea == null || currentDungeon != null)
            currentArea = getArea()
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        onHypixel = false
        inSkyblock = false
        currentArea = null
        currentDungeon = null
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) {
        inSkyblock = false
        currentArea = null
        currentDungeon = null
    }

    /**
     * Taken from [SBC](https://github.com/Harry282/Skyblock-Client/blob/main/src/main/kotlin/skyblockclient/utils/LocationUtils.kt)
     */
    @SubscribeEvent
    fun onConnect(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        onHypixel = mc.runCatching {
            !event.isLocal && ((thePlayer?.clientBrand?.lowercase()?.contains("hypixel")
                ?: currentServerData?.serverIP?.lowercase()?.contains("hypixel")) == true)
        }.getOrDefault(false)
    }


    /**
     * Returns the current area from the tab list info.
     * If no info can be found return null.
     */
    private fun getArea(): String? {
        if (mc.isSingleplayer) return "Singleplayer" // debugging
        if (!inSkyblock) return null
        val netHandlerPlayClient: NetHandlerPlayClient = mc.thePlayer?.sendQueue ?: return null
        val list = netHandlerPlayClient.playerInfoMap ?: return null

        if (currentDungeon != null) {
            return "Catacombs${currentDungeon!!.floor}" +
                    if (currentDungeon!!.inBoss) { if (getPhase() != null) "P${getPhase()}" else "Boss" } else ""
        }

        var area: String? = null
        var extraInfo: String? = null

        for (entry in list) {
            val areaText = entry?.displayName?.unformattedText ?: continue

            if (areaText.startsWith("Area: ")) {
                area = areaText.substringAfter("Area: ")
                if (!area.contains("Private Island")) break
            }
            if (areaText.contains("Owner:")) extraInfo = areaText.substringAfter("Owner:")
        }
        return if (area == null) null else area + (extraInfo ?: "")
    }
}