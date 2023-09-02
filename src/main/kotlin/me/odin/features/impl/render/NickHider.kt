package me.odin.features.impl.render

import me.odin.features.Category
import me.odin.features.Module
import me.odin.features.settings.impl.StringSetting

/**
 * @see me.odin.mixin.MixinFontRenderer
 */
object NickHider : Module(
    name = "Nick Hider",
    category = Category.RENDER,
    description = "Replace your name, color codes work.",
    tag = TagType.NEW
) {

    val nick: String by StringSetting("Nick")

}