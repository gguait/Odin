package me.odin.features.general

import me.odin.Odin
import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.events.ClientSecondEvent
import me.odin.events.RenderEntityModelEvent
import me.odin.utils.render.OutlineUtils
import me.odin.utils.VecUtils.noSqrt3DDistance
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object HighLights {
    private inline val hightlightList get() = Odin.miscConfig.hightlightList
    
    private var color = config.highlightColor.toJavaColor()

    private var currentEntities = mutableListOf<Entity>()
    @SubscribeEvent
    fun onSecond(event: ClientSecondEvent) {
        if (!config.highlight) return
        currentEntities.removeAll { it.isDead }
        color = config.highlightColor.toJavaColor()
        mc.theWorld?.loadedEntityList?.filterIsInstance<EntityArmorStand>()?.forEach { entity ->
            if (!hightlightList.any { entity.name.lowercase().contains(it) } || currentEntities.contains(entity)) return@forEach

            val entities =
                mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.entityBoundingBox.expand(1.0, 5.0, 1.0))
                    .filter { it != null && it !is EntityArmorStand && it != mc.thePlayer }
                    .sortedByDescending { noSqrt3DDistance(it, entity) }
            if (entities.isEmpty()) return@forEach
            currentEntities.add(entities.first())
        }
    }

    @SubscribeEvent
    fun onRenderEntityModel(event: RenderEntityModelEvent) {
        if (!config.highlight || !currentEntities.contains(event.entity)) return
        if (!mc.thePlayer.canEntityBeSeen(event.entity)) return
        OutlineUtils.outlineEntity(
            event,
            config.highlightThickness,
            color,
            config.highlightCancelHurt
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