package com.oxipro.cmu.versionsupport.hologram.v26;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;

final class MountPackets {

    static ClientboundSetPassengersPacket create(int vehicleId, int[] passengers) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(vehicleId);
        buf.writeVarInt(passengers.length);
        for (int passenger : passengers) {
            buf.writeVarInt(passenger);
        }
        return ClientboundSetPassengersPacket.STREAM_CODEC.decode(buf);
    }

    private MountPackets() {
    }
}
