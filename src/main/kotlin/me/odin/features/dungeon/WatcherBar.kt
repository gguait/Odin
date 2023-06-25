package me.odin.features.dungeon

import me.odin.Odin.Companion.config
import me.odin.utils.skyblock.LocationUtils
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraft.entity.boss.BossStatus
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import kotlin.math.roundToInt

object WatcherBar{
    private var name: String? = null

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!config.watcherBar || !DungeonUtils.inDungeons) return
        if (!stripControlCodes(BossStatus.bossName).contains("The Watcher")) return

        val health = BossStatus.healthScale
        val floor = LocationUtils.currentDungeon?.floor ?: return

        if (health < 0.05) {
            name = null
            return
        }
        val amount = 12 + floor.floorNumber
        name = " " + (amount * health).roundToInt() + "/" + amount
    }

    @SubscribeEvent
    fun onRenderBossHealth(event: RenderGameOverlayEvent) {
        if (
            !DungeonUtils.inDungeons ||
            event.type != RenderGameOverlayEvent.ElementType.BOSSHEALTH ||
            name == null
        ) return
        if (stripControlCodes(BossStatus.bossName) != "The Watcher") return
        BossStatus.bossName += name
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        name = null
    }
}