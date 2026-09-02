package com.oxipro.cmu.versionsupport;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Locale;

public class particle_v1_20_R4 implements ParticleSupport {

    @Override
    public boolean isParticle(String name) {
        return resolve(name) != null;
    }

    @Override
    public void spawnParticle(Player player, float x, float y, float z, String particle) {
        player.spawnParticle(require(particle), x, y, z, 1);
    }

    @Override
    public void spawnParticle(World world, float x, float y, float z, String particle) {
        world.spawnParticle(require(particle), x, y, z, 1);
    }

    @Override
    public String getForVersion(String v18, String v19, String v12, String v13, String v205) {
        return v205;
    }

    private static Particle require(String name) {
        Particle particle = resolve(name);
        if (particle == null) {
            throw new IllegalArgumentException("Unknown particle: " + name);
        }
        return particle;
    }

    private static Particle resolve(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        String enumName = name;
        int colon = name.indexOf(':');
        if (colon >= 0) {
            enumName = name.substring(colon + 1);
        }
        try {
            return Particle.valueOf(enumName.toUpperCase(Locale.ROOT).replace('-', '_').replace('.', '_'));
        } catch (IllegalArgumentException ignored) {
        }
        NamespacedKey key = colon >= 0
                ? NamespacedKey.fromString(name.toLowerCase(Locale.ROOT))
                : NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT));
        if (key == null) {
            return null;
        }
        return Registry.PARTICLE_TYPE.get(key);
    }
}
