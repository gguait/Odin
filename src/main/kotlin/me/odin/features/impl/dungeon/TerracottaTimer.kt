package me.odin.features.impl.dungeon

import me.odin.events.impl.BlockChangeEvent
import me.odin.events.impl.ServerTickEvent
import me.odin.features.Category
import me.odin.features.Module
import me.odin.utils.VecUtils.addVec
import me.odin.utils.render.Color
import me.odin.utils.render.world.RenderUtils
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TerracottaTimer : Module(
    name = "Terracotta Timer",
    description = "Displays the time until the terracotta spawns",
    category = Category.DUNGEON,
    tag = TagType.NEW
) {
    private data class Terracotta(val pos: Vec3, var time: Int)
    private var terrasSpawning = mutableListOf<Terracotta>()

    @SubscribeEvent
    fun onBlockPacket(event: BlockChangeEvent) {
        if (!DungeonUtils.isFloor(6) || !DungeonUtils.inBoss) return
        if (!event.update.block.isFlowerPot) return
        terrasSpawning.add(
            Terracotta(
                Vec3(event.pos).addVec(.5, 1.5, .5),
                1500
            )
        )
        modMessage("added terracotta")
    }

    @SubscribeEvent
    fun onServerTick(event: ServerTickEvent) {
        terrasSpawning.forEach { it.time -= 5 }
        terrasSpawning.removeAll { it.time <= 0 }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        val iterator = terrasSpawning.iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            RenderUtils.drawStringInWorld(
                "${it.time / 100.0}s",
                it.pos,
                getColor(it.time / 100.0).rgba,
                renderBlackBox = false,
                increase = false,
                depthTest = false,
                .03f
            )
        }
    }

    private fun getColor(time: Double): Color {
        return when {
            time > 5.0 -> Color(0, 170, 0)
            time > 2.0 -> Color(255, 170, 0)
            else -> Color(170, 0, 0)
        }
    }

}