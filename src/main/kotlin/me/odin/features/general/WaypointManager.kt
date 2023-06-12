package me.odin.features.general

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.config
import me.odin.Odin.Companion.waypointConfig
import me.odin.events.ClientSecondEvent
import me.odin.ui.waypoint.WaypointGUI
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.render.RenderUtils
import me.odin.utils.skyblock.LocationUtils.currentArea
import net.minecraft.util.StringUtils
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.util.*

object WaypointManager {

    private inline val waypoints get() = waypointConfig.waypoints
    private var temporaryWaypoints = mutableListOf<Waypoint>()

    fun addWaypoint(name: String, x: Int, y: Int, z: Int, color: Color) {
        if (currentArea == null) return modMessage("You are not in Skyblock.")

        waypoints.getOrPut(currentArea!!) { mutableListOf() }.add(Waypoint(name, x, y, z, color, null))
        waypointConfig.saveConfig()
    }

    fun removeWaypoint(name: String) {
        if (currentArea == null) return modMessage("You are not in Skyblock.")

        val matchingWaypoint = waypoints[currentArea]?.find { StringUtils.stripControlCodes(it.name).lowercase() == name }
        waypoints[currentArea]?.removeAll { matchingWaypoint == it }
        waypointConfig.saveConfig()
    }

    fun removeWaypoint(waypoint: Waypoint) {
        if (currentArea == null) return modMessage("You are not in Skyblock.")
        waypoints[currentArea]?.remove(waypoint)
    }

    fun clearWaypoints() {
        if (currentArea == null) return modMessage("You are not in Skyblock.")
        waypoints[currentArea]?.clear()
        waypointConfig.saveConfig()
    }

    fun addTempWaypoint(name: String, x: Int, y: Int, z: Int) {
        if (currentArea == null) return modMessage("You are not in Skyblock.")
        val randomColor = Random().run { Color(nextInt(255), nextInt(255), nextInt(255)) }
        temporaryWaypoints.add(Waypoint(name, x, y, z, randomColor, System.currentTimeMillis() + 100000))
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (!config.waypoints || currentArea == null) return

        if (temporaryWaypoints.isNotEmpty()) {
            temporaryWaypoints.forEach {
                it.renderBeacon(event.partialTicks)
            }
        }

        if (waypointConfig.waypoints.isNotEmpty()) {
            waypoints[currentArea]?.forEach {
                if (it.shouldShow) it.renderBeacon(event.partialTicks)
            }
        }
    }

    /**
     * Handles removing temporary waypoints without needing to do it every render event
     */
    @SubscribeEvent
    fun onSecond(event: ClientSecondEvent) {
        if (temporaryWaypoints.isEmpty()) return
        temporaryWaypoints.removeAll { it.shouldRemove() }
    }

    data class Waypoint(
        var name: String,
        var x: Int,
        var y: Int,
        var z: Int,
        var color: Color = Color.RED,
        private val removalTime: Long? = null,
        var shouldShow: Boolean = true,
    ) {
        fun renderBeacon(partialTicks: Float) =
            RenderUtils.renderCustomBeacon(name, x + .5, y + .5, z + .5, color, partialTicks)

        fun shouldRemove(): Boolean =
            if (removalTime == null) false else removalTime <= System.currentTimeMillis()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        GlobalScope.launch {
            delay(4000)
            if (currentArea != null) WaypointGUI.updateElements(currentArea!!)
        }
    }
}