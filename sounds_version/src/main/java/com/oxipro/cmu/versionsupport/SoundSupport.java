package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import javax.annotation.Nullable;

import static com.oxipro.cmu.versionsupport.VersionMapping.resolveNmsVersion;

public interface SoundSupport {

    /**
     * Check if the given sound is valid for the current server version.
     *
     * @param name sound name.
     * @return true if given name is an existing sound.
     */
    boolean isSound(String name);

    /**
     * Get sound by name.
     *
     * @param name sound name.
     * @return Sound with given name. Null if does not exist.
     */
    @Nullable
    Sound getSound(String name);

    /**
     * Get sound by name if valid or fallback sound.
     *
     * @param name        sound name.
     * @param alternative alternative if not found.
     * @return sound with given name or alternative if not found.
     */
    Sound getSoundOr(String name, Sound alternative);

    /**
     * Get sound for current server version.
     *
     * @param v1_8  sound name. Will return this if server version is 1.8.
     * @param v1_12 sound name. Will return this is server version is in range [1.9,1.12]
     * @param v1_13 sound name. Will return this if server version is in range [1.13,)
     * @return right sound for current version.
     */
    @Nullable
    Sound getForCurrentVersion(String v1_8, String v1_12, String v1_13);

    class SupportBuilder {

        /**
         * @return support for your server version. Null if not supported.
         */
        public static SoundSupport load() {
            try {
                String version = resolveNmsVersion();
                Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Resolved NMS version: " + version);

                if (version == null) {
                    Bukkit.getLogger().severe("[CMU Debug] PlayerUtils - Unknown server version: " + Bukkit.getBukkitVersion());
                    return null;
                }

                Class<?> c;
                try {
                    String className = "com.oxipro.cmu.versionsupport.sound_" + version;
                    Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Trying class: " + className);
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Version-specific class not found, trying major version...");
                    try {
                        String majorVersion = version.substring(0, version.lastIndexOf("_R"));
                        String className = "com.oxipro.cmu.versionsupport.sound_" + majorVersion;
                        Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Trying major class: " + className);
                        c = Class.forName(className);
                    } catch (ClassNotFoundException | StringIndexOutOfBoundsException ex) {
                        String className = "com.oxipro.cmu.versionsupport.sound_Default";
                        Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Trying fallback class: " + className);
                        c = Class.forName(className);
                    }
                }

                Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Successfully loaded: " + c.getName());
                return (SoundSupport) c.getDeclaredConstructor().newInstance();

            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("[CMU Debug] PlayerUtils - No suitable class found (not even Default): " + e.getMessage());
                return null;
            } catch (ReflectiveOperationException e) {
                Bukkit.getLogger().severe("[CMU Debug] PlayerUtils - Failed to instantiate: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
