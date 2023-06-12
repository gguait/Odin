package me.odin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.odin.commands.impl.*
import me.odin.config.MiscConfig
import me.odin.config.OdinConfig
import me.odin.config.WaypointConfig
import me.odin.events.ClientSecondEvent
import me.odin.features.dungeon.*
import me.odin.features.general.*
import me.odin.features.m7.*
import me.odin.features.qol.*
import me.odin.utils.*
import me.odin.utils.render.RenderUtils
import me.odin.utils.skyblock.ChatUtils
import me.odin.utils.skyblock.LocationUtils
import me.odin.utils.skyblock.PlayerUtils
import me.odin.utils.skyblock.dungeon.Dungeon
import me.odin.utils.skyblock.dungeon.DungeonUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import java.io.File

@Mod(
    modid = Odin.MOD_ID,
    name = Odin.NAME,
    version = Odin.VERSION,
    clientSideOnly = true
)
class Odin {

    private val openAppKeyBinding = KeyBinding(
        "Open Separate Application",
        Keyboard.KEY_NONE,
        "Your Mod Name"
    )

    @EventHandler
    fun init(event: FMLInitializationEvent) {

        config.init()

        ClientRegistry.registerKeyBinding(openAppKeyBinding)

        listOf(
            DragonBoxes,
            DragonTimer,
            BlessingDisplay,
            GuildCommands,
            PartyCommands,
            HighLights,
            BrokenHype,
            AutoSprint,
            LocationUtils,
            ChatUtils,
            WatcherBar,
            DragonDeathCheck,
            Server,
            KuudraAlerts,
            AbiphoneBlocker,
            PlayerUtils,
            FPS,
            VanqNotifier,
            DeployableTimer,
            Waypoints,
            Reminders,
            P5Waypoints,
            RenderUtils,
            WishAlert,
            TeammatesOutline,
            WaypointManager,
            TerminalTimes,
            KeyHighLight,
            Dungeon,
            CanClip,
            BlazeAtunement,
            Camera,
            GyroRange,
            NoCursorReset,
            DungeonUtils,
            Welcome,
            this
        ).forEach {
            MinecraftForge.EVENT_BUS.register(it)
        }


        for (command in commandList) {
            ClientCommandHandler.instance.registerCommand(command)
        }
    }

    @EventHandler
    fun postInit(event: FMLPostInitializationEvent) = runBlocking {
        launch(Dispatchers.IO) {
            miscConfig.loadConfig()
            waypointConfig.loadConfig()
        }
    }



    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        tickRamp++

        if (display != null) {
            mc.displayGuiScreen(display)
            display = null
        }

        if (tickRamp % 20 == 0) {
            if (mc.thePlayer != null) MinecraftForge.EVENT_BUS.post(ClientSecondEvent())
            tickRamp = 0
        }
    }

    @SubscribeEvent
    fun onWorldLoad(@Suppress("UNUSED_PARAMETER") event: WorldEvent.Load) {
        tickRamp = 18
    }

    companion object {
        const val MOD_ID = "Odin"
        const val NAME = "Odin"
        const val VERSION = "1.0.0"

        val mc: Minecraft = Minecraft.getMinecraft()

        var config = OdinConfig
        val miscConfig = MiscConfig(File(mc.mcDataDir, "config/odin"))
        val waypointConfig = WaypointConfig(File(mc.mcDataDir, "config/odin"))
        var display: GuiScreen? = null
        var tickRamp = 0

        val commandList = listOf(
            OdinCommand,
            HightlightCommand,
            WaypointCommand,
            BlacklistCommand,

        )
    }
}