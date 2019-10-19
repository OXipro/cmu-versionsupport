package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public interface SoundSupport {

    /**
     * @param name sound name.
     * @return true if given name is an existing sound.
     */
    boolean isSound(String name);

    /**
     * @param name sound name.
     * @return Sound with given name. Null if does not exist.
     */
    @Nullable
    Sound getSound(String name);

    /**
     * @param name        sound name.
     * @param alternative alternative if not found.
     * @return sound with given name or alternative if not found.
     */
    Sound getSoundOr(String name, Sound alternative);

    /**
     * @param v1_8  sound name
     * @param v1_12 sound name
     * @param v1_13 sound name
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
            String version = Bukkit.getServer().getClass().getName().split(".")[3];
            Class c;
            try {
                c = Class.forName("com.andrei1058.spigot.versionsupport.sound." + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (SoundSupport) c.getConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
    }
}
