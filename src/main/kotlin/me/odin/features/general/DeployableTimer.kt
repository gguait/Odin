package me.odin.features.general

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DeployableTimer {
    private enum class Deployables (
        val texture: String,
        val displayName: String,
        val renderName: String,
        val priority: Int,
        val duration: Int,
        val img: ResourceLocation
    )  {
        Warning(
            "ewogICJ0aW1lc3RhbXAiIDogMTY0NjY4NzMwNjIyMywKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJlMmJmNmMxZWMzMzAyNDc5MjdiYTYzNDc5ZTU4NzJhYzY2YjA2OTAzYzg2YzgyYjUyZGFjOWYxYzk3MTQ1OCIKICAgIH0KICB9Cn0=",
            "Warning Flare",
            "§aWarning Flare",
            3,
            180000,
            ResourceLocation("Odin", "firework.png")
        ),

        Alert(
            "ewogICJ0aW1lc3RhbXAiIDogMTY0NjY4NzMyNjQzMiwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQyYmY5ODY0NzIwZDg3ZmQwNmI4NGVmYTgwYjc5NWM0OGVkNTM5YjE2NTIzYzNiMWYxOTkwYjQwYzAwM2Y2YiIKICAgIH0KICB9Cn0=",
            "Alert Flare",
            "§9Alert Flare",
            5,
            180000,
            ResourceLocation("Odin", "firework.png")
        ),

        SOS(
            "ewogICJ0aW1lc3RhbXAiIDogMTY0NjY4NzM0NzQ4OSwKICAicHJvZmlsZUlkIiA6ICI0MWQzYWJjMmQ3NDk0MDBjOTA5MGQ1NDM0ZDAzODMxYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdha2xvb24iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAwNjJjYzk4ZWJkYTcyYTZhNGI4OTc4M2FkY2VmMjgxNWI0ODNhMDFkNzNlYTg3YjNkZjc2MDcyYTg5ZDEzYiIKICAgIH0KICB9Cn0=",
            "SOS Flare",
            "§5§lSOS Flare",
            7,
            180000,
            ResourceLocation("Odin", "firework.png")
        ),

        Radiant(
            "RADIANTPLACEHOLDERTEXTURE",
            "Radiant",
            "§aRadiant Orb",
            1,
            30000,
            ResourceLocation("Odin", "RADIANTPOWERORB.png")
        ),

        Mana(
            "MANAFLUXPLACEHOLDERTEXTURE",
            "Mana" ,
            "§9Mana Flux Orb",
            2,
            30000,
            ResourceLocation("Odin", "MANAFLUXPOWERORB.png")
        ),

        Overflux(
            "OVERFLUXPLACEHOLDERTEXTURE",
            "Overflux",
            "§5Overflux Orb",
            4,
            30000,
            ResourceLocation("Odin", "OVERFLUXPOWERORB.png")
        ),

        Plasma(
            "PLASMAFLUXPLACEHOLDERTEXTURE",
            "Plasma",
            "§d§lPlasmaflux",
            5,
            60000,
            ResourceLocation("Odin", "PLASMAPOWERORB.png")
        ),
    }

    class Deployable(
        val priority: Int,
        val duration: Int,
        val entity: EntityArmorStand,
        val renderName: String,
        val img: ResourceLocation,
        val timeAdded: Long = System.currentTimeMillis()
    )

    private val currentDeployables = mutableListOf<Deployable>()
    private val orbRegex = Regex("(.+) (\\d+)s")

    @OptIn(DelicateCoroutinesApi::class, DelicateCoroutinesApi::class)
    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        if (event.entity !is EntityArmorStand) return
        GlobalScope.launch {
            delay(100)
            val armorStand = event.entity as EntityArmorStand
            val name = stripControlCodes(armorStand.name)
            val texture =
                armorStand.inventory
                ?.get(4)
                ?.tagCompound
                ?.getCompoundTag("SkullOwner")
                ?.getCompoundTag("Properties")
                ?.getTagList("textures", 10)
                ?.getCompoundTagAt(0)
                ?.getString("Value")

            if (Deployables.values().any { it.texture == texture }) {
                val flare = Deployables.values().first { it.texture == texture }
                val priority = flare.priority
                currentDeployables.add(Deployable(priority, flare.duration, armorStand, flare.renderName, flare.img))
                currentDeployables.sortByDescending { it.priority }
                lines = Triple(null, null, null)
            } else if (Deployables.values().any { name.startsWith(it.displayName)}) {
                val orb = Deployables.values().first { name.startsWith(it.displayName)}
                val priority = orb.priority
                val time = orbRegex.find(name)?.groupValues?.get(2)?.toInt() ?: return@launch
                currentDeployables.add(Deployable(priority, time * 1000, armorStand, orb.renderName, orb.img))
                currentDeployables.sortByDescending { it.priority }
                lines = Triple(null, null, null)
            }
        }
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        currentDeployables.clear()
        lines = Triple(null, null, null)
    }

    var lines: Triple<String?, String?, ResourceLocation?> = Triple(null, null, null)

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || currentDeployables.size == 0) return
        val currentMillis = System.currentTimeMillis()
        val d = currentDeployables.first()
        val timeLeft = (d.timeAdded + d.duration - currentMillis) / 1000
        if (timeLeft <= 0 || d.entity.isDead) {
            currentDeployables.remove(d)
            currentDeployables.sortBy { it.priority }
            lines = Triple(null, null, null)
            return
        }
        lines = Triple(d.renderName, "§e${timeLeft}s", d.img)
    }
}