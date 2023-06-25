package me.odin.features.general

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.waypointConfig
import me.odin.commands.impl.WaypointCommand.randomColor
import me.odin.ui.waypoint.WaypointGUI
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.render.RenderUtils
import me.odin.utils.skyblock.LocationUtils.currentArea
import net.minecraft.util.StringUtils
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object WaypointManager {

    private inline val waypoints get() = waypointConfig.waypoints
    private var temporaryWaypoints = mutableListOf<Pair<Waypoint, Long>>()

    fun addWaypoint(name: String, x: Int, y: Int, z: Int, color: Color) =
        addWaypoint(Waypoint(name, x, y, z, color))

    fun addWaypoint(waypoint: Waypoint, area: String = currentArea!!) {
        waypoints.getOrPut(area) { mutableListOf() }.add(waypoint)
        waypointConfig.saveConfig()
    }

    fun removeWaypoint(name: String) {
        val matchingWaypoint = waypoints[currentArea]?.find { StringUtils.stripControlCodes(it.name).lowercase() == name } ?: return
        removeWaypoint(matchingWaypoint)
    }

    fun removeWaypoint(waypoint: Waypoint) {
        if (currentArea == null) return modMessage("You are not in Skyblock.")
        waypoints[currentArea]?.remove(waypoint)
        waypointConfig.saveConfig()
    }

    fun clearWaypoints() {
        if (currentArea == null) return modMessage("You are not in Skyblock.")
        waypoints[currentArea]?.clear()
        waypointConfig.saveConfig()
    }

    fun addTempWaypoint(name: String, x: Int, y: Int, z: Int) {
        if (currentArea == null) return modMessage("You are not in Skyblock.")
        temporaryWaypoints.add(Pair(Waypoint(name, x, y, z, randomColor), System.currentTimeMillis() + 60000))
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (!config.waypoints || currentArea == null) return

        temporaryWaypoints.removeAll {
            if (it.second >= System.currentTimeMillis()) {
                it.first.renderBeacon(event.partialTicks)
                false
            } else true
        }

        waypoints[currentArea]?.forEach {
            if (it.shouldShow) it.renderBeacon(event.partialTicks)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        temporaryWaypoints.clear()
        GlobalScope.launch {
            delay(4000)
            if (currentArea != null) WaypointGUI.updateElements(currentArea!!)
        }
    }

    data class Waypoint(
        var name: String,
        var x: Int,
        var y: Int,
        var z: Int,
        var color: Color = Color.RED,
        var shouldShow: Boolean = true,
    ) {
        constructor(name: String, vec3: Vec3i, color: Color) : this(name, vec3.x, vec3.y, vec3.z, color, true)

        fun renderBeacon(partialTicks: Float) = RenderUtils.renderCustomBeacon(name, x + .5, y + .5, z + .5, color, partialTicks)
    }
}