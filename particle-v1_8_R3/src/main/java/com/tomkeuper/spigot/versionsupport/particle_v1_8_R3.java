package com.tomkeuper.spigot.versionsupport;

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
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void spawnParticle(World w, float x, float y, float z, String particle) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle), true, x, y, z, 0, 0, 0, 0, 10);
        for (Player p : w.getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void spawnParticle(World w, String particle, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle), true, x, y, z, offsetX, offsetY, offsetZ, speed, amount);
        for (Player p : w.getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void spawnRedstoneParticle(World w, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int amount) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, x, y, z, offsetX, offsetY, offsetZ, speed, amount);
        for (Player p : w.getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public String getForVersion(String v18, String v19, String V12, String V13) {
        return v18;
    }
}
