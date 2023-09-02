package me.odin.features.impl.floor7

import me.odin.features.Category
import me.odin.features.Module
import me.odin.utils.clock.Clock
import me.odin.utils.skyblock.ChatUtils
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DecoyDeadMessage : Module(
    name = "Decoy Dead Message",
    description = "Sends a message in party chat when a decoy dies",
    category = Category.FLOOR7,
    tag = TagType.NEW
) {
    private var lastSentMessage = Clock(500)

    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        if (event.entity.name != "Decoy " || event.entity.isDead || !lastSentMessage.hasTimePassed()) return
        ChatUtils.partyMessage("Decoy killed.")
        lastSentMessage.update()
    }
}