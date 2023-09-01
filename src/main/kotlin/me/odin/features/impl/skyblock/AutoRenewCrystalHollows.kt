package me.odin.features.impl.skyblock

import me.odin.features.Category
import me.odin.features.Module
import me.odin.utils.skyblock.ChatUtils

object AutoRenewCrystalHollows : Module(
    name = "Auto-Renew Hollows Pass",
    category = Category.SKYBLOCK,
    tag = TagType.NEW
) {

    init {
        onMessage("Your pass to the Crystal Hollows will expire in 1 minute".toRegex(), { enabled }) {
            ChatUtils.sendCommand("purchasecrystallhollowspass");
        }
    }

}