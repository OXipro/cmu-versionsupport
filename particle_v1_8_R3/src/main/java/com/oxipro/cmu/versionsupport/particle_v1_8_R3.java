package com.oxipro.cmu.versionsupport;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class particle_v1_8_R3 implements ParticleSupport {

    @Override
    public boolean isParticle(String name) {
        try {
            EnumParticle.valueOf(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void spawnParticle(Player player, float x, float y, float z, String particle) {
        send(player, packet(particle, x, y, z));
    }

    @Override
    public void spawnParticle(World world, float x, float y, float z, String particle) {
        PacketPlayOutWorldParticles packet = packet(particle, x, y, z);
        for (Player player : world.getPlayers()) {
            send(player, packet);
        }
    }

    @Override
    public String getForVersion(String v18, String v19, String v12, String v13, String v205) {
        return v18;
    }

    private static PacketPlayOutWorldParticles packet(String particle, float x, float y, float z) {
        return new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle), true, x, y, z, 0, 0, 0, 0, 1);
    }

    private static void send(Player player, PacketPlayOutWorldParticles packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
