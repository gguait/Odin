package me.odin.utils.skyblock.dungeon

import me.odin.features.impl.render.ClickGUIModule
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.skyblock.PlayerUtils.posX
import me.odin.utils.skyblock.PlayerUtils.posZ
import me.odin.utils.skyblock.ScoreboardUtils

// In future maybe add stats about the dungeon like time elapsed, deaths, total secrets etc.
// could add some system to look back at previous runs.
class Dungeon {

    lateinit var floor: Floor
    var inBoss = false
    private fun getBoss(floor: Int): Boolean {
         return when (floor) {
            1 -> posX > -71 && posZ > -39
            2, 3, 4 ->  posX > -39 && posZ > -39
            5, 6 ->  posX > -39 && posZ > -7
            7 ->  posX > -7 && posZ > -7
            else -> false
        }
    }

    init {
        getCurrentFloor()

        ClickGUIModule.execute(500) {
            if (getBoss(floor.floorNumber)) {
                inBoss = true
                destroyExecutor()
            }
        }
    }

    private fun getCurrentFloor() {
        for (i in ScoreboardUtils.sidebarLines) {

            val line = ScoreboardUtils.cleanSB(i)

            if (line.contains("The Catacombs (")) {
                runCatching { floor = Floor.valueOf(line.substringAfter("(").substringBefore(")")) }
                .onFailure { modMessage("Could not get correct floor. Please report this.") }
            }
        }
    }

    enum class Floor {
        E, F1, F2, F3, F4, F5, F6, F7,
        M1, M2, M3, M4, M5, M6, M7;

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
}