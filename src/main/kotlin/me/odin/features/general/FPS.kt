package me.odin.features.general

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object FPS {
    private var lastClear = System.currentTimeMillis()

    @SubscribeEvent
    fun onFallingBlock(event: EntityJoinWorldEvent) {
        if (event.entity !is EntityFallingBlock || !config.fps) return
        event.entity.setDead()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (System.currentTimeMillis() - lastClear >= 30000L) {
            val world = mc.theWorld ?: return
            val currentEnts = world.playerEntities.toMutableList()
            currentEnts.forEach {
                if (it.isDead) {
                    world.playerEntities.remove(it)
                }
                if (isNullVec(it)) {
                    world.removeEntityFromWorld(it.entityId)
                }
            }
            lastClear = System.currentTimeMillis()
        }
    }

    private fun isNullVec(entity: Entity): Boolean = entity.posX == 0.0 && entity.posY == 0.0 && entity.posZ == 0.0
}