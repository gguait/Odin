package me.odin.utils.skyblock.dungeon

import me.odin.utils.Wrappers
import me.odin.utils.skyblock.ItemUtils
import me.odin.utils.skyblock.LocationUtils
import me.odin.utils.skyblock.LocationUtils.currentDungeon
import me.odin.utils.skyblock.PlayerUtils
import me.odin.utils.skyblock.ScoreboardUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.StringUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

object DungeonUtils : Wrappers() {

    val inDungeons
        get() = LocationUtils.inSkyblock && currentDungeon != null

    val inBoss
        get() = currentDungeon?.inBoss ?: false

    fun isFloor(vararg options: Int): Boolean {
        for (option in options) {
            if (currentDungeon?.floor?.floorNumber == option) return true
        }
        return false
    }

    fun getPhase(): Int? {
        if (!isFloor(7) || !inBoss) return null

        return when {
            posY > 210 -> 1
            posY > 155 -> 2
            posY > 100 -> 3
            posY > 45 -> 4
            else -> if (currentDungeon?.floor == Dungeon.Floor.M7) 5 else 4
        }
    }

    enum class Classes(
        val letter: String,
        val code: String,
        val color: Color
    ) {
        ARCHER("A", "§6", Color(255, 170, 0)),
        MAGE("M", "§5", Color(170, 0, 170)),
        BERSERKER("B", "§4", Color(170, 0, 0)),
        HEALER("H", "§a", Color(85, 255, 85)),
        TANK("T", "§2", Color(0, 170, 0))
    }
    val isGhost: Boolean get() = ItemUtils.getItemIndexInInventory("Haunt", true) != -1
    var teammates: List<Pair<EntityPlayer, Classes>> = emptyList()
    private var lastUpdate: Long = System.currentTimeMillis()

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (!inDungeons || System.currentTimeMillis() - lastUpdate < 1000) return
        teammates = getDungeonTeammates()
        lastUpdate += 1000
    }

    private fun getDungeonTeammates(): List<Pair<EntityPlayer, Classes>> {
        val temp = mutableListOf<Pair<EntityPlayer, Classes>>()
        ScoreboardUtils.sidebarLines.forEach {
            val line = StringUtils.stripControlCodes(it)
            if (!line.startsWith("[")) return@forEach
            val symbol = line[1].toString()
            val name = PlayerUtils.removeSymbols(line.split(" ")[1])
            mc.theWorld.playerEntities.find { player ->
                player.name == name && player != mc.thePlayer
            }?.let { a ->
                temp.add(Pair(a, Classes.values().find { classes -> classes.letter == symbol }!!))
            }
        }
        return temp
    }
}