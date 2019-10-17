package com.andrei1058.spigot.versionsupport.sound;

import org.bukkit.Sound;

public class v1_13_R1 extends v1_8_R3 {
    @Override
    public Sound getForCurrentVersion(String v1_8, String v1_12, String v1_13) {
        return getSound(v1_13);
    }
}
