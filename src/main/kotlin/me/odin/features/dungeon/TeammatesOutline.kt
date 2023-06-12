package me.odin.features.dungeon

import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.mc
import me.odin.events.RenderEntityModelEvent
import me.odin.utils.render.OutlineUtils
import me.odin.utils.render.RenderUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.max

object TeammatesOutline {
    @SubscribeEvent
    fun onRenderEntityModel(event: RenderEntityModelEvent) {
        if (
            !config.teammatesOutline ||
            !DungeonUtils.inDungeons ||
            !DungeonUtils.teammates.any {it.first == event.entity } ||
            (!config.teammatesOutlineInBoss && DungeonUtils.inBoss)
        ) return
        if (!config.teammatesOutlineWhenVisible && mc.thePlayer.canEntityBeSeen(event.entity)) return
        val color = DungeonUtils.teammates.first {it.first == event.entity }.second.color
        OutlineUtils.outlineEntity(
            event,
            config.teammateThickness,
            color
        )
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (
            !config.teammatesOutline ||
            !DungeonUtils.inDungeons ||
            (!config.teammatesOutlineInBoss && DungeonUtils.inBoss)
        ) return
        DungeonUtils.teammates.forEach {
            if (mc.thePlayer.canEntityBeSeen(it.first)) return
            RenderUtils.drawStringInWorld(
                RenderUtils.renderVec(it.first).addVector(0.0, 2.7, 0.0),
                "${it.second.code}${it.first.name}",
                false,
                max(1.5f, (mc.thePlayer.getDistanceToEntity(it.first) / 15)),
                false
            )
        }
    }
}