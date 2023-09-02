package me.odin.features.impl.dungeon

import me.odin.Odin.Companion.mc
import me.odin.events.impl.RenderEntityModelEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.BooleanSetting
import me.odin.features.settings.impl.NumberSetting
import me.odin.utils.VecUtils.addVec
import me.odin.utils.render.world.OutlineUtils
import me.odin.utils.render.world.RenderUtils
import me.odin.utils.render.world.RenderUtils.renderVec
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.max

object TeammatesOutline : Module(
    "Teammate Highlight",
    category = Category.DUNGEON,
    description = "Enhances visibility of your dungeon teammates and their name tags."
) {
    private val thickness: Float by NumberSetting("Line Width", 2f, 1.0, 5.0, 0.5)
    private val whenVisible: Boolean by BooleanSetting("When Visible")
    private val inBoss: Boolean by BooleanSetting("In boss")
    private val outline: Boolean by BooleanSetting("Outline", true)

    @SubscribeEvent
    fun onRenderEntityModel(event: RenderEntityModelEvent) {
        if (!DungeonUtils.inDungeons || event.entity == mc.thePlayer || !outline) return
        if (!DungeonUtils.teammates.any { it.first == event.entity } || (!inBoss && DungeonUtils.inBoss)) return
        if (!whenVisible && mc.thePlayer.canEntityBeSeen(event.entity)) return
        val color = DungeonUtils.teammates.find { it.first == event.entity }?.second?.color ?: return

        OutlineUtils.outlineEntity(
            event,
            thickness,
            color,
            false
        )
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!DungeonUtils.inDungeons || (!inBoss && DungeonUtils.inBoss)) return
        DungeonUtils.teammates.forEach {
            if (mc.thePlayer.canEntityBeSeen(it.first) || it.first == mc.thePlayer) return
            RenderUtils.drawStringInWorld(
                "${it.second.code}${it.first.name}",
                it.first.renderVec.addVec(y = 2.7),
                depthTest = false,
                increase = false,
                renderBlackBox = false,
                scale = max(0.03f, mc.thePlayer.getDistanceToEntity(it.first) / 250)
            )
        }
    }
}