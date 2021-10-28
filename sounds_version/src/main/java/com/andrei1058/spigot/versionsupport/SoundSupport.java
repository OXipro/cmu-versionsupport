package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import javax.annotation.Nullable;

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
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static SoundSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> c;
            try {
                c = Class.forName("com.andrei1058.spigot.versionsupport.sound_" + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (SoundSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
