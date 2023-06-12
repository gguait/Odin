package me.odin.ui.waypoint

import cc.polyfrost.oneconfig.renderer.font.Fonts
import cc.polyfrost.oneconfig.utils.dsl.*
import me.odin.Odin.Companion.waypointConfig
import me.odin.features.general.WaypointManager
import me.odin.utils.gui.GuiUtils.scaleFactor
import me.odin.utils.gui.GuiUtils.scaleWithMouse
import me.odin.utils.gui.GuiUtils.scaledHeight
import me.odin.utils.gui.GuiUtils.scaledWidth
import me.odin.utils.gui.GuiUtils.scissor
import me.odin.utils.gui.GuiUtils.translateWithMouse
import me.odin.utils.gui.MouseHandler
import me.odin.utils.gui.animations.EaseInOut
import me.odin.utils.gui.animations.LinearAnimation
import me.odin.utils.skyblock.LocationUtils.currentArea
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Mouse
import java.awt.Color
import java.io.IOException
import java.util.*
import kotlin.math.floor
import kotlin.math.sign

object WaypointGUI : GuiScreen() {
    private var area: String? = null // rename
    private var list = listOf<WaypointElement>()

    private var scrollTarget = 0f // idk a better name
    private var scrollOffset = 0f
    private val scrollAnimation = LinearAnimation(200)

    var mouseHandler = MouseHandler()
    private var settingMenu = false
    private val settingAnimation = EaseInOut(250)

    override fun initGui() {
        area = currentArea
        area?.let { updateElements(it) }

        settingMenu = false
        scrollTarget = 0f
        scrollOffset = 0f
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        nanoVG(true) {
            translateWithMouse(mouseHandler, scaledWidth / 4f, scaledHeight / 4f)
            scaleWithMouse(mouseHandler, scaleFactor, scaleFactor)

            drawDropShadow(0, 0, 480, 264, 10f, 1f, 10f)
            drawRoundedRectVaried(0, 25, 480, 239, Color(21, 22, 23, 235).rgb, 0, 0, 10, 10)

            scissor(0f, 25f, 480f, 239f) {
                scrollOffset = scrollAnimation.getValue(scrollOffset, scrollTarget)
                var currentY = 35f - scrollOffset
                for (waypoint in list) {
                    waypoint.y = currentY
                    currentY += waypoint.drawScreen(this)
                }
            }

            val animY = settingAnimation.getValue(25f, 50f, !settingMenu)

            drawRoundedRectVaried(0, 0, 480, animY, Color(21, 22, 23).rgb, 10, 10, 0, 0)
            drawLine(0, animY, 480, animY, 1.5, Color(30, 32, 34).rgb)

            drawText("Add Waypoint", 16, 13.25, Color.LIGHT_GRAY.rgb, 10, Fonts.REGULAR)
            val buttonColor = (if (mouseHandler.isAreaHovered(10f, 5f, 78.5f, 15f)) Color(38, 40, 42) else Color(30, 32, 34)).rgb
            drawHollowRoundedRect(10, 5, 78, 15, 5, buttonColor, 0.75)

            //drawRoundedRect(445, 0, animX, animY, 10f, Color(21, 22, 23).rgb)
           //drawDropShadow(445, 0, animX, animY, 10f, 1f, 10f)

            val color = if (mouseHandler.isAreaHovered(455f, 5f, 15f, 15f)) Color.LIGHT_GRAY.rgb else Color.WHITE.rgb
            drawSVG("/assets/odin/Settings.svg", 455, 5, 15, 15, color, 36, javaClass)
            // 455
        }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseHandler.isAreaHovered(455f, 5f, 15f, 15f)) {
            if (settingAnimation.start()) settingMenu = !settingMenu
            return
        } else if (mouseHandler.isAreaHovered(10f, 5f, 78.5f, 15f)) {
            val randomColor = Random().run { Color(nextInt(255), nextInt(255), nextInt(255)) }
            WaypointManager.addWaypoint("§fWaypoint",
                floor(mc.thePlayer.posX).toInt(), floor(mc.thePlayer.posY).toInt(), floor(mc.thePlayer.posZ).toInt() - 1,
                randomColor
            )
            updateElements()
            return
        }
        for (i in list) {
            i.mouseClicked(mouseButton, mouseHandler)
        }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        for (i in list) {
            i.keyTyped(typedChar, keyCode)
        }
        super.keyTyped(typedChar, keyCode)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    @Throws(IOException::class)
    override fun handleMouseInput() {
        super.handleMouseInput()

        if (Mouse.getEventDWheel() != 0) {
            val amount = Mouse.getEventDWheel().sign * -16 //.coerceIn(0f, getMaxScrollHeight())
            scrollTarget = (scrollTarget + amount).coerceAtMost(getMaxScrollHeight()).coerceAtLeast(0f)
            scrollAnimation.start(true)
        }
    }

    private fun getMaxScrollHeight(): Float {
        return -229 + list.size * 40f
    }

    fun updateElements(area: String = currentArea ?: "") {
        val waypointList = waypointConfig.waypoints[area] ?: return
        list = waypointList.map { WaypointElement(it) }
    }
}