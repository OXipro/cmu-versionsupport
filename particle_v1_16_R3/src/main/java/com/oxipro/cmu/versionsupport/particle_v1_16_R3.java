package com.oxipro.cmu.versionsupport;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class particle_v1_16_R3 implements ParticleSupport {

    @Override
    public boolean isParticle(String name) {
        try {
            Particle.valueOf(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void spawnParticle(Player player, float x, float y, float z, String particle) {
        player.spawnParticle(Particle.valueOf(particle), x, y, z, 1);
    }

    @Override
    public void spawnParticle(World world, float x, float y, float z, String particle) {
        world.spawnParticle(Particle.valueOf(particle), x, y, z, 1);
    }

    @Override
    public String getForVersion(String v18, String v19, String v12, String v13, String v205) {
        return v13;
    }
}
