package me.odin.utils

import me.odin.Odin.Companion.mc
import me.odin.events.ReceivePacketEvent
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.network.play.server.S01PacketJoinGame
import net.minecraft.network.play.server.S03PacketTimeUpdate
import net.minecraft.network.play.server.S37PacketStatistics
import net.minecraft.util.MathHelper
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object Server {

    private var prevTime = 0L
    var averageTps = 20.0
    var averagePing = 0.0
    private var isPinging = false
    private var pingStartTime = 0L

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        prevTime = 0L
        averageTps = 20.0
        averagePing = 0.0
    }

    private var tickRamp = 20
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        tickRamp++
        if (tickRamp % 40 != 0) return
        sendPing()
    }

    @SubscribeEvent
    fun onPacket(event: ReceivePacketEvent) {
        when (event.packet) {
            is S37PacketStatistics -> {
                isPinging = false
                averagePing = (System.nanoTime() - pingStartTime) / 1e6 * 0.4 + averagePing * 0.6
            }

            is S03PacketTimeUpdate -> {
                isPinging = false
                if (prevTime != 0L) {
                    averageTps = MathHelper.clamp_float( // try this
                        (20000 / (System.currentTimeMillis() - prevTime + 1)).toFloat(),
                        0F,
                        20F
                    ) * 0.182 + averageTps * 0.818
                }
                prevTime = System.currentTimeMillis()
            }

            is S01PacketJoinGame -> {
                isPinging = false
                averagePing = 0.0
            }
        }
    }
    private fun sendPing() {
        if (isPinging || mc.thePlayer == null) return
        pingStartTime = System.nanoTime()
        isPinging = true
        PacketUtils.sendPacketNoEvent(
            C16PacketClientStatus(
                C16PacketClientStatus.EnumState.REQUEST_STATS
            )
        )
    }
}
