package me.odin.features.general

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.events.RenderEntityModelEvent
import me.odin.utils.render.OutlineUtils
import me.odin.utils.VecUtils.xzDistance
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityBlaze
import net.minecraft.entity.monster.EntityPigZombie
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

object BlazeAtunement {

    private var currentBlazes = mutableListOf<Pair<Entity, Color>>()

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.atunementOutline) return

        mc.theWorld?.loadedEntityList?.filterIsInstance<EntityArmorStand>()?.forEach { entity ->
            if (currentBlazes.any { it.second == entity }) return@forEach
            val name = stripControlCodes(entity.name)
            val color = when {
                name.contains("CRYSTAL ♨") -> Color(85, 250, 236)
                name.contains("ASHEN ♨") -> Color(45, 45, 45)
                name.contains("AURIC ♨") -> Color(206, 219, 57)
                name.contains("SPIRIT ♨") -> Color(255, 255, 255)
                else -> return@forEach
            }

            val entities =
                mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.entityBoundingBox.expand(0.0, 3.0, 0.0))
                    .filter {
                        it != null &&
                        it !is EntityArmorStand &&
                        it != mc.thePlayer &&
                        (it is EntityBlaze || it is EntitySkeleton || it is EntityPigZombie)
                    }
                    .sortedByDescending { xzDistance(it, entity) }
            if (entities.isEmpty()) return@forEach
            currentBlazes.add(Pair(entities[0], color))
        }
    }

    @SubscribeEvent
    fun onRenderEntityModel(event: RenderEntityModelEvent) {
        if (!config.atunementOutline || currentBlazes.size == 0) return
        if (!currentBlazes.any { it.first == event.entity }) return
        val color = currentBlazes.firstOrNull { it.first == event.entity }?.second ?: return
        OutlineUtils.outlineEntity(
            event,
            config.atunementOutlineThickness,
            color
        )
    }
}