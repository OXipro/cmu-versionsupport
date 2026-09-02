package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import static com.oxipro.cmu.versionsupport.VersionMapping.resolveNmsVersion;

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
            try {
                String version = resolveNmsVersion();
                Bukkit.getLogger().info("[CMU Debug] Particle - Resolved NMS version: " + version);

                if (version == null) {
                    Bukkit.getLogger().severe("[CMU Debug] Particle - Unknown server version: " + Bukkit.getBukkitVersion());
                    return null;
                }

                Class<?> c;
                try {
                    String className = "com.oxipro.cmu.versionsupport.particle_" + version;
                    Bukkit.getLogger().info("[CMU Debug] Particle - Trying class: " + className);
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    try {
                        String majorVersion = version.substring(0, version.lastIndexOf("_R"));
                        String className = "com.oxipro.cmu.versionsupport.particle_" + majorVersion;
                        Bukkit.getLogger().info("[CMU Debug] Particle - Trying major class: " + className);
                        c = Class.forName(className);
                    } catch (ClassNotFoundException | StringIndexOutOfBoundsException ex) {
                        Bukkit.getLogger().severe("[CMU Debug] Particle - No suitable class found for: " + version);
                        return null;
                    }
                }

                Bukkit.getLogger().info("[CMU Debug] Particle - Successfully loaded: " + c.getName());
                return (ParticleSupport) c.getDeclaredConstructor().newInstance();

            } catch (ReflectiveOperationException e) {
                Bukkit.getLogger().severe("[CMU Debug] Particle - Failed to instantiate: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
