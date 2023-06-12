package me.odin.features.dungeon

import me.odin.Odin.Companion.mc
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object CanClip {

    var canClip = false

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || !mc.thePlayer.isSneaking || !DungeonUtils.inBoss) {
            canClip = false
            return
        }
        val x = mc.thePlayer.posX % 1
        val z = mc.thePlayer.posZ % 1
        canClip = x in 0.24..0.26 || x in 0.74..0.76 || z in 0.24..0.26 || z in 0.74..0.76
    }
}