package me.odin.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.odin.events.impl.PacketSentEvent;
import me.odin.events.impl.ReceivePacketEvent;
import me.odin.utils.ServerUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {NetworkManager.class}, priority = 800)
public class MixinNetworkManager {

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new ReceivePacketEvent(packet)))
            ci.cancel();
    }

    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (!ServerUtils.INSTANCE.handleSendPacket(packet)) {
            if (MinecraftForge.EVENT_BUS.post(new PacketSentEvent(packet)))
                ci.cancel();
        }
    }
}