package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public interface ParticleSupport {

    /**
     * @param name particle name.
     * @return true if given name is an existing particle.
     */
    default boolean isParticle(String name) {
        try {
            Particle.valueOf(name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    default void spawnParticle(World w, float x, float y, float z, String particle) {
        w.spawnParticle(Particle.valueOf(particle), x, y, z, 1);
    }

    default void spawnParticle(World w, String particle, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int amount) {
        w.spawnParticle(Particle.valueOf(particle), x, y, z, amount, offsetX, offsetY, offsetZ, speed);
    }

    default void spawnRedstoneParticle(World w, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int amount) {
        w.spawnParticle(Particle.REDSTONE, x, y, z, amount, offsetX, offsetY, offsetZ, speed);
    }

    default String getForVersion(String v18, String v19, String V12, String V13) {
        return V13;
    }

    class SupportBuilder {

        /**
         * @return particle support for your server version. Null if not supported.
         */
        @Nullable
        public static ParticleSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> c;
            try {
                c = Class.forName("com.andrei1058.spigot.versionsupport.particle_" + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (ParticleSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
