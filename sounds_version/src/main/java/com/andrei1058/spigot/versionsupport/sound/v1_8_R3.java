package com.andrei1058.spigot.versionsupport.sound;

import com.andrei1058.spigot.versionsupport.SoundSupport;
import org.bukkit.Sound;

import javax.annotation.Nullable;

public class v1_8_R3 implements SoundSupport {
    public boolean isSound(String name) {
        try {
            Sound.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Nullable
    public Sound getSound(String name) {
        Sound s = null;
        try {
            s = Sound.valueOf(name.toUpperCase());
        } catch (Exception ignored) {
        }
        return s;
    }

    public Sound getSoundOr(String name, Sound alternative) {
        Sound s = null;
        try {
            s = Sound.valueOf(name.toUpperCase());
        } catch (Exception ignored) {
        }
        return s == null ? alternative : s;
    }

    public Sound getForCurrentVersion(String v1_8, String v1_12, String v1_13) {
        return getSound(v1_8);
    }
}
