package com.tomkeuper.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public interface ParticleSupport {

    /**
     * Check if the given string is a particle.
     *
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

    /**
     * Spawn a particle at the given location.
     *
     * @param particle particle by name.
     */
    default void spawnParticle(World w, float x, float y, float z, String particle) {
        w.spawnParticle(Particle.valueOf(particle), x, y, z, 1);
    }

    /**
     * Spawn particles at the given location in the given box (offsets).
     *
     * @param speed  movement speed.
     * @param amount particles amount. Some particles uses this param as color.
     */
    default void spawnParticle(World w, String particle, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int amount) {
        w.spawnParticle(Particle.valueOf(particle), x, y, z, amount, offsetX, offsetY, offsetZ, speed);
    }

    /**
     * Spawn a red stone particle at the given location in the given box (offsets).
     *
     * @param speed movement speed.
     * @param color red stone color.
     */
    default void spawnRedstoneParticle(World w, float x, float y, float z, int offsetX, int offsetY, int offsetZ, int speed, int color) {
        w.spawnParticle(Particle.REDSTONE, x, y, z, color, offsetX, offsetY, offsetZ, speed);
    }

    /**
     * Get the right particle for current server version.
     *
     * @param v18 particle by name for 1.8 server.
     * @param v19 particle by name for 1.9 server.
     * @param V12 particle by name for [1.10, 1.12]
     * @param V13 particle by name for 1.13 and newer.
     */
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
                c = Class.forName("com.tomkeuper.spigot.versionsupport.particle_" + version);
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
