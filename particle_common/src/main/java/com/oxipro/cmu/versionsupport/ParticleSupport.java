package com.oxipro.cmu.versionsupport;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface ParticleSupport {

    /**
     * Check if the given string is a valid particle for this server version.
     *
     * @param name particle name (enum constant or namespaced key on 1.20.5+).
     * @return true if the particle exists.
     */
    boolean isParticle(String name);

    /**
     * Spawn a particle visible only to the given player.
     */
    void spawnParticle(Player player, float x, float y, float z, String particle);

    /**
     * Spawn a particle in the world (visible to players in range).
     */
    void spawnParticle(World world, float x, float y, float z, String particle);

    /**
     * Pick the particle name that matches the current server version.
     *
     * @param v18  1.8
     * @param v19  1.9
     * @param v12  1.10–1.12
     * @param v13  1.13–1.20.4
     * @param v205 1.20.5 and newer
     */
    String getForVersion(String v18, String v19, String v12, String v13, String v205);

    class SupportBuilder {

        /**
         * @return particle support for the running server version, or null if unsupported.
         */
        @Nullable
        public static ParticleSupport load() {
            return VersionMapping.load(ParticleSupport.class, "com.oxipro.cmu.versionsupport.particle_");
        }
    }
}
