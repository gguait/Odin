package me.odin.utils.skyblock.dungeon

import me.odin.events.ClientSecondEvent
import me.odin.utils.Wrappers
import me.odin.utils.skyblock.ScoreboardUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

// In future maybe add stats about the dungeon like time elapsed, deaths, total secrets etc. could add some system to look back at previous runs.
class Dungeon : Wrappers() {

    init {
        getCurrentFloor()
    }

    val floor
        get() = dungeonFloor
    val inBoss
        get () = boss

    private fun getCurrentFloor() {
        ScoreboardUtils.sidebarLines.forEach {
            val line = ScoreboardUtils.cleanSB(it)
            if (dungeonFloor == null && line.contains("The Catacombs (")) {
                dungeonFloor = try {
                    Floor.valueOf(line.substringAfter("(").substringBefore(")"))
                } catch (_ : IllegalArgumentException) {
                    null
                }
            }
        }
    }

    enum class Floor {
        E, F1, F2, F3, F4, F5, F6, F7, M1, M2, M3, M4, M5, M6, M7;

        val floorNumber: Int
            get() {
                return when (this) {
                    E -> 0
                    F1, M1 -> 1
                    F2, M2 -> 2
                    F3, M3 -> 3
                    F4, M4 -> 4
                    F5, M5 -> 5
                    F6, M6 -> 6
                    F7, M7 -> 7
                }
            }

        val isInMM: Boolean
            get() {
                return when (this) {
                    E, F1, F2, F3, F4, F5, F6, F7 -> false
                    M1, M2, M3, M4, M5, M6, M7 -> true
                }
            }
    }

    companion object {

        private var dungeonFloor: Floor? = null
        private var boss: Boolean = false

        @SubscribeEvent
        fun onSecond(event: ClientSecondEvent) {
            boss = when (dungeonFloor?.floorNumber) {
                1 -> posX > -71 && posZ > -39
                2, 3, 4 -> posX > -39 && posZ > -39
                5, 6 -> posX > -39 && posZ > -7
                7 -> posX > -7 && posZ > -7
                else -> false
            }
        }
    }
}