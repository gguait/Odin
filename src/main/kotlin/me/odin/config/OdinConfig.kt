package me.odin.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import me.odin.Odin.Companion.miscConfig
import me.odin.hud.*

object OdinConfig : Config(Mod("Odin", ModType.SKYBLOCK, "/assets/odin/LogoSmall.png"), "odin.json") {

    fun init() {
        initialize()
    }




    // Dungeon

    @Switch(
        name = "Dungeon key highlight",
        description = "Dungeon key highlight",
        category = "Dungeon",
        subcategory = "KeyHighlight",
        size = 2
    )
    var keyESP = false

    @Switch(
        name = "Highlights your dungeon teammates",
        description = "Highlights your dungeon teammates",
        category = "Dungeon",
        subcategory = "Teammate Highlight",
        size = 2
    )
    var teammatesOutline = false

    @Checkbox(
        name = "In Boss",
        description = "Outlines teammates in boss as well",
        category = "Dungeon",
        subcategory = "Teammate Highlight",
        size = 2
    )
    var teammatesOutlineInBoss = false

    @Checkbox(
        name = "When Visible",
        description = "Outlines teammates while they're visible as well",
        category = "Dungeon",
        subcategory = "Teammate Highlight",
        size = 2
    )
    var teammatesOutlineWhenVisible = true

    @Slider(
        name = "Teammate Thickness",
        description = "Teammate Thickness",
        category = "Dungeon",
        subcategory = "Teammate Highlight",
        min = 5f,
        max = 20f
    )
    var teammateThickness = 5f

    @Switch(
        name = "Shows how many blood mobs are left",
        description = "Shows how many blood mobs are left",
        category = "Dungeon",
        subcategory = "Watcher Bar",
        size = 2
    )
    var watcherBar = false

    @Checkbox(
        name = "Power Blessing",
        description = "Displays the power blessing",
        category = "Dungeon",
        subcategory = "Blessing Display",
        size = 2
    )
    var powerBlessing = true

    @Checkbox(
        name = "Time Blessing",
        description = "Displays the time blessing",
        category = "Dungeon",
        subcategory = "Blessing Display",
        size = 2
    )
    var timeBlessing = true

    @Checkbox(
        name = "Stone Blessing",
        description = "Displays the stone blessing",
        category = "Dungeon",
        subcategory = "Blessing Display",
        size = 2
    )
    var stoneBlessing = false

    @Checkbox(
        name = "Life Blessing",
        description = "Displays the life blessing",
        category = "Dungeon",
        subcategory = "Blessing Display",
        size = 2
    )
    var lifeBlessing = false

    @Checkbox(
        name = "Wisdom Blessing",
        description = "Displays the wisdom blessing",
        category = "Dungeon",
        subcategory = "Blessing Display",
        size = 2
    )
    var wisdomBlessing = false

    @HUD(
        name = "Blessing Display HUD",
        category = "Dungeon",
        subcategory = "Blessing Display"
    )
    var powerDisplayHud: PowerDisplayHud = PowerDisplayHud()

    @HUD(
        name = "Can Clip Hud",
        category = "Dungeon",
        subcategory = "Can Clip"
    )
    var canClipHud: CanClipHud = CanClipHud()



    // General

    @Checkbox(
        name = "Front Camera",
        description = "Turns on or off the front camera in f5 mode",
        category = "General",
        subcategory = "Camera",
        size = 2
    )
    var frontCamera = true

    @Switch(
        name = "Attunement Outline",
        description = "Attunement Outline",
        category = "General",
        subcategory = "Blaze Slayer",
        size = 2
    )
    var atunementOutline = false

    @Slider(
        name = "Attunement Outline Thickness",
        description = "Attunement Outline Thickness",
        category = "General",
        subcategory = "Blaze Slayer",
        min = 5f,
        max = 20f
    )
    var atunementOutlineThickness = 5f

    @Switch(
        name = "You can add whatever mob you want into the list /highlight",
        description = "You can add whatever mob you want into the list /highlight",
        category = "General",
        subcategory = "Highlight",
        size = 2
    )
    var highlight = false

    @Slider(
        name = "Highlight Thickness",
        description = "Highlight Thickness",
        category = "General",
        subcategory = "Highlight",
        min = 5f,
        max = 20f
    )
    var highlightThickness = 5f

    @Color(
        name = "Highlight Color",
        description = "Highlight Color",
        category = "General",
        subcategory = "Highlight"
    )
    var highlightColor = OneColor(255, 255, 255)

    @Button(
        name = "Adds the star mob star to your highlights list so all star mobs are highlighted",
        description = "",
        category = "General",
        subcategory = "Highlight",
        text = "Add star mobs to highlights list",
        size = 2
    )
    fun addStar() {
        if (miscConfig.hightlightList.contains("✯")) return
        miscConfig.hightlightList.add("✯")
        miscConfig.saveAllConfigs()
    }

    @Switch(
        name = "Improves ur frames",
        description = "Fixes certain Hypixel elements to improve performance.",
        category = "General",
        subcategory = "FPS",
        size = 2
    )
    var fps = false

    @Switch(
        name = "Custom guild commands use !help in guild chat",
        description = "Custom guild commands use !help in guild chat",
        category = "General",
        subcategory = "Chat commands",
        size = 2
    )
    var guildCommands = false

    @Switch(
        name = "Custom party commands use !help in party chat",
        description = "Custom party commands use !help in party chat",
        category = "General",
        subcategory = "Chat commands",
        size = 2
    )
    var partyCommands = false

    @Switch(
        name = "Responds to anyone in guild chat saying gm/gn",
        description = "Responds to anyone in guild chat saying gm/gn",
        category = "General",
        subcategory = "Chat commands",
        size = 2
    )
    var guildGM = false

    @Info(
        text = "/blacklist add {ign}, remove, list, clear",
        category = "General",
        subcategory = "Chat commands",
        type = InfoType.INFO
    )
    var blacklistInfo = ""


    @Switch(
        name = "Allows the creation of waypoints /od waypoint",
        description = "Allows the creation of waypoints /od waypoint",
        category = "General",
        subcategory = "Waypoints",
        size = 2
    )
    var waypoints = false

    @HUD(
        name = "Deployable HUD",
        category = "General",
        subcategory = "Deployable Timer"
    )
    var deployableHud: DeployableHud = DeployableHud()


    //M7

    @Switch(
        name = "Creates custom and decently accurate boxes in p5",
        description = "Creates custom and decently accurate boxes in p5",
        category = "M7",
        subcategory = "Dragon Boxes",
        size = 2
    )
    var dragonBoxes = false


    @Slider(
        name = "Line Width",
        min = 1f,
        max = 5f,
        category = "M7",
        subcategory = "Dragon Boxes"
    )
    var dragonBoxesLineWidth = 2f

    @Switch(
        name = "Shows where to decoy and gyro in p5",
        description = "Shows where to decoy and gyro in p5",
        category = "M7",
        subcategory = "Phase5 Waypoints",
        size = 2
    )
    var p5Waypoint = false

    @Switch(
        name = "Shows how long a terminal took to complete",
        description = "Shows how long a terminal took to complete",
        category = "M7",
        subcategory = "Terminal timer",
        size = 2
    )
    var termTimer = false

    @Switch(
        name = "Shows when a M7 dragon will spawn",
        description = "Shows when a M7 dragon will spawn",
        category = "M7",
        subcategory = "Dragon Timer",
        size = 2
    )
    var dragonTimer = false

    @HUD(
        name = "Dragon Timer Hud",
        category = "M7",
        subcategory = "Dragon Timer"
    )
    var dragonTimerHud: DragonTimerHud = DragonTimerHud()


    // QOL

    @Switch(
        name = "No Cursor Reset",
        description = "Prevents your cursor from resetting when you open a gui",
        category = "QOL",
        subcategory = "No Cursor Reset",
        size = 2
    )
    var noCursorReset = false

    @Switch(
        name = "Gyro Range",
        description = "Renders a circle for the range of your gyro",
        category = "QOL",
        subcategory = "Gyro Range",
        size = 2
    )
    var gyroRange = false

    @Slider(
        name = "Gyro Range Thickness",
        min = 0f,
        max = 10f,
        category = "QOL",
        subcategory = "Gyro Range"
    )
    var gyroThickness = 10f

    @Slider(
        name = "Gyro Range Steps",
        category = "QOL",
        subcategory = "Gyro Range",
        min = 20f,
        max = 80f,
        step = 1
    )
    var gyroSteps = 40f

    @Color(
        name = "Gyro Range Color",
        category = "QOL",
        subcategory = "Gyro Range"
    )
    var gyroColor = OneColor(192, 64, 192, 128)


    @Switch(
        name = "Chat in portal",
        description = "Chat in portal",
        category = "QOL",
        subcategory = "Portal",
        size = 2
    )
    var portalFix = false

    @Switch(
        name = "Blocks annoying abiphone calls",
        description = "Blocks annoying abiphone calls",
        category = "QOL",
        subcategory = "Abiphone Blocker",
        size = 2
    )
    var abiphoneBlocker = false

    @Switch(
        name = "Toggle Sprint",
        description = "Toggle Sprint",
        category = "QOL",
        subcategory = "Auto Sprint",
        size = 2
    )
    var autoSprint = false

    @Switch(
        name = "Automatically alerts when your hype is broken",
        description = "Automatically alerts when your hype is broken",
        category = "QOL",
        subcategory = "Broken Hype",
        size = 2
    )
    var brokenHype = false

    @Checkbox(
        name = "Play Sound",
        category = "QOL",
        subcategory = "Broken Hype",
        size = 1
    )
    var brokenHypePlaySound = false

    @Checkbox(
        name = "Display Title",
        category = "QOL",
        subcategory = "Broken Hype",
        size = 1
    )
    var brokenHypeShowTitle = false

    @Switch(
        name = "Kuudra Alerts",
        description = "Shows messages on screen on specific events in kuudra",
        category = "QOL",
        subcategory = "Notifications",
        size = 2
    )
    var kuudraAlerts = false

    @Checkbox(
        name = "Edrag Reminder",
        description = "Reminder to equip edrag when p5 starts",
        category = "QOL",
        subcategory = "Notifications",
        size = 2
    )
    var dragonReminder = false

    @Switch(
        name = "Mask Alert",
        description = "Alerts whenever you pop a bonzo/spirit mask",
        category = "QOL",
        subcategory = "Notifications",
        size = 2
    )
    var maskAlert = false

    @Switch(
        name = "Ready Reminder",
        description = "Reminds you to ready up when the dungeon starts",
        category = "QOL",
        subcategory = "Notifications",
        size = 2,
    )
    var readyReminder = false

    @Switch(
        name = "Ultimates Reminder",
        description = "Reminds you to use your dungeon ultimate at specific events",
        category = "QOL",
        subcategory = "Notifications",
        size = 2
    )
    var ultReminder = false

    @Switch(
        name = "Wish Alert",
        description = "Alerts whenever a teammate is low HP",
        category = "QOL",
        subcategory = "Notifications",
        size = 2
    )
    var wishAlert = false

    @Slider(
        name = "hp% to wish at",
        description = "hp% to wish at",
        category = "QOL",
        subcategory = "Notifications",
        min = 5f,
        max = 100f
    )
    var healthPrecentage = 30f

    @Switch(
        name = "Alerts you when a vanquisher spawns",
        description = "Alerts you when a vanquisher spawns",
        category = "General",
        subcategory = "Vanq Notifier",
        size = 2
    )
    var vanqNotifier = false

    @Checkbox(
        name = "Send message in all chat",
        category = "General",
        subcategory = "Vanq Notifier",
        size = 1
    )
    var vanqNotifierAC = false

    @Checkbox(
        name = "Send message in party chat",
        category = "General",
        subcategory = "Vanq Notifier",
        size = 1
    )
    var vanqNotifierPC = false

    @HUD(
        name = "Server HUD",
        category = "QOL",
        subcategory = "Server"
    )
    var serverHUD: ServerHud = ServerHud()
}