package me.odin.features

import me.odin.Odin.Companion.mc
import me.odin.events.impl.*
import me.odin.features.impl.dungeon.*
import me.odin.features.impl.floor7.*
import me.odin.features.impl.floor7.p3.ArrowAlign
import me.odin.features.impl.floor7.p3.SimonSays
import me.odin.features.impl.floor7.p3.TerminalSolver
import me.odin.features.impl.render.*
import me.odin.features.impl.skyblock.*
import me.odin.ui.hud.HudElement
import me.odin.utils.clock.Executor
import me.odin.utils.render.gui.nvg.drawNVG
import net.minecraft.network.Packet
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Class that contains all Modules and huds
 * @author Aton
 */
object ModuleManager {
    data class PacketFunction<T : Packet<*>>(val type: Class<T>, val function: (T) -> Unit, val shouldRun: () -> Boolean)
    data class MessageFunction(val filter: Regex, val function: (String) -> Unit)

    val packetFunctions = mutableListOf<PacketFunction<Packet<*>>>()
    val messageFunctions = mutableListOf<MessageFunction>()
    val huds = arrayListOf<HudElement>()
    val executors = ArrayList<Executor>()

    val modules: ArrayList<Module> = arrayListOf(
        BlessingDisplay,
        KeyESP,
        TeammatesOutline,
        WatcherBar,
        PersonalDragon,
        CustomEnd,
        EscrowFix,
        Camera,

        ClickGUIModule,
        ESP,
        CPSDisplay,
        VanqNotifier,
        PartyCommands,
        GuildCommands,
        PrivateCommands,
        DragonBoxes,
        DragonTimer,
        LeapHelper,
        NecronDropTimer,
        DecoyDeadMessage,
        AutoSprint,
        GyroRange,
        KuudraAlerts,
        NoCursorReset,
        Reminders,

        BPS,
        TerminalTimes,
        Waypoints,
        Server,
        DeployableTimer,
        CanClip,
        NoRender,
        RelicAnnouncer,
        TerminalSolver,
        SimonSays,
        NickHider,
        DragonHitboxes,
        ArrowAlign,
        TerracottaTimer,
        MimicMessage,
        AutoRenewCrystalHollows,
        AutoDungeonReque,
        //DragonDeathCheck
    )

    @SubscribeEvent
    fun onReceivePacket(event: ReceivePacketEvent) {
        packetFunctions.filter { it.type.isInstance(event.packet) && it.shouldRun.invoke() }.forEach { it.function(event.packet) }
    }

    @SubscribeEvent
    fun onSendPacket(event: PacketSentEvent) {
        packetFunctions.filter { it.type.isInstance(event.packet) }.forEach { it.function(event.packet) }
    }

    @SubscribeEvent
    fun onChatPacket(event: ChatPacketEvent) {
        messageFunctions.filter { event.message matches it.filter }.forEach { it.function(event.message) }
    }

    @SubscribeEvent
    fun activateModuleKeyBinds(event: PreKeyInputEvent) {
        modules.filter { it.keyCode == event.keycode }.forEach { it.onKeybind() }
    }

    @SubscribeEvent
    fun activateModuleMouseBinds(event: PreMouseInputEvent) {
        modules.filter { it.keyCode + 100 == event.button }.forEach { it.onKeybind() }
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Pre) {
        if (mc.currentScreen != null || event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        drawNVG {
            for (i in 0 until huds.size) {
                huds[i].draw(this, false)
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        executors.removeAll { it.run() }
    }

    fun getModuleByName(name: String): Module? = modules.firstOrNull { it.name.equals(name, true) }
}