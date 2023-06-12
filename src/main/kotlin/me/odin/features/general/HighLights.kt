package me.odin.features.general

import me.odin.Odin
import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.events.RenderEntityModelEvent
import me.odin.utils.render.OutlineUtils
import me.odin.utils.VecUtils.noSqrt3DDistance
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object HighLights {

    private inline val espList get() = Odin.miscConfig.espList
    
    private var color = config.highlightColor.toJavaColor()

    private var currentEntities = HashMap<Int, Entity>()
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.highlight) return
        color = config.highlightColor.toJavaColor()
        mc.theWorld?.loadedEntityList?.filterIsInstance<EntityArmorStand>()?.forEach { entity ->
            if (
                !espList.any { entity.name.lowercase().contains(it) } ||
                currentEntities.containsKey(entity.entityId)
            ) return@forEach

            val entities =
                mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.entityBoundingBox.expand(1.0, 5.0, 1.0))
                    .filter { it != null && it !is EntityArmorStand && it != mc.thePlayer }
                    .sortedByDescending { noSqrt3DDistance(it, entity) }
            if (entities.isEmpty()) return@forEach
            currentEntities[entity.entityId] = entities[0]
        }
    }

    @SubscribeEvent
    fun onRenderEntityModel(event: RenderEntityModelEvent) {
        if (!config.highlight || !currentEntities.containsValue(event.entity)) return
        if (!mc.thePlayer.canEntityBeSeen(event.entity)) return
        OutlineUtils.outlineEntity(
            event,
            config.highlightThickness,
            color
        )
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        clear()
    }

    fun clear() {
        currentEntities.clear()
    }
}