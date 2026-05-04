package com.oxipro.cmu.versionsupport;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;

public class particle_v1_16_R3 implements ParticleSupport {

    @Override
    public void spawnRedstoneParticle(World w, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int color) {
        w.spawnParticle(Particle.REDSTONE, x, y, z, color, offsetX, offsetY, offsetZ, speed, new Particle.DustOptions(Color.fromRGB(color), 1));
    }
}

