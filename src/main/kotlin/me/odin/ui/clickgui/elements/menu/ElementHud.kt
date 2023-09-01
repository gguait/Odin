package me.odin.ui.clickgui.elements.menu

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.polyfrost.oneconfig.renderer.font.Fonts
import me.odin.features.settings.impl.HudSetting
import me.odin.ui.clickgui.ClickGUI
import me.odin.ui.clickgui.elements.Element
import me.odin.ui.clickgui.elements.ElementType
import me.odin.ui.clickgui.elements.ModuleButton
import me.odin.ui.clickgui.util.ColorUtil
import me.odin.ui.clickgui.util.ColorUtil.brighter
import me.odin.ui.clickgui.util.ColorUtil.buttonColor
import me.odin.ui.clickgui.util.ColorUtil.clickGUIColor
import me.odin.ui.clickgui.util.ColorUtil.textColor
import me.odin.ui.clickgui.util.HoverHandler
import me.odin.ui.hud.EditHUDGui
import me.odin.utils.render.gui.MouseUtils.isAreaHovered
import me.odin.utils.render.gui.animations.impl.ColorAnimation
import me.odin.utils.render.gui.nvg.*

/**
 * Renders all the modules.
 *
 * Backend made by Aton, with some changes
 * Design mostly made by Stivais
 *
 * @author Stivais, Aton
 * @see [Element]
 */
class ElementHud(parent: ModuleButton, setting: HudSetting) : Element<HudSetting>(
    parent, setting, ElementType.DUAL
) {
    override val isHovered: Boolean
        get() = setting.displayToggle && isAreaHovered(x + w - 30f, y + 5f, 21f, 20f)

    private val isShortcutHovered: Boolean
        get() {
            return if (setting.displayToggle) isAreaHovered(x + w - 60f, y + 5f, 21f, 20f)
            else isAreaHovered(x + w - 30f, y + 5f, 21f, 20f)
        }


    private val colorAnim = ColorAnimation(250)
    private val hover = HoverHandler(0, 150)

    override fun draw(nvg: NVG) {
        nvg {
            rect(x, y, w, h, ColorUtil.elementBackground)
            text(name, x + 6f, y + 18f, textColor, 16f, Fonts.REGULAR)

            var offset = 30f
            if (setting.displayToggle) {
                hover.handle(x + w - 30f, y + 5f, 21f, 20f)
                val color = colorAnim.get(clickGUIColor, buttonColor, setting.enabled).brighter(1 + hover.percent() / 500f)

                dropShadow(x + w - offset, y + 5f, 21f, 20f, 10f, 0.75f, 5f)
                rect(x + w - offset, y + 5f, 21f, 20f, color, 5f)
                rectOutline(x + w - offset, y + 5f, 21f, 20f, clickGUIColor, 5f, 1.5f)
                offset = 60f
            }
            NanoVGHelper.INSTANCE.drawSvg(this.context,
                "/assets/odin/ui/clickgui/MovementIcon.svg", x + w - offset, y + 5f, 20f, 20f, 20f, javaClass
            )
        }
    }

    override fun mouseClicked(mouseButton: Int): Boolean {
        if (mouseButton == 0) {
            when {
                isHovered -> if (colorAnim.start()) setting.enabled = !setting.enabled
                isShortcutHovered -> ClickGUI.swapScreens(EditHUDGui)
                else -> return false
            }
            return true
        }
        return false
    }
}