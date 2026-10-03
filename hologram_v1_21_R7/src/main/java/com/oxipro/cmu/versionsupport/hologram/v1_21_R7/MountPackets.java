package com.oxipro.cmu.versionsupport.hologram.v1_21_R7;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutMount;

final class MountPackets {

    static PacketPlayOutMount create(int vehicleId, int[] passengers) {
        PacketDataSerializer buf = new PacketDataSerializer(Unpooled.buffer());
        writeVarInt(buf, vehicleId);
        writeVarInt(buf, passengers.length);
        for (int passenger : passengers) {
            writeVarInt(buf, passenger);
        }
        return PacketPlayOutMount.a.decode(buf);
    }

    private static void writeVarInt(PacketDataSerializer buf, int value) {
        while ((value & -128) != 0) {
            buf.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        buf.writeByte(value);
    }

    private MountPackets() {
    }
}
