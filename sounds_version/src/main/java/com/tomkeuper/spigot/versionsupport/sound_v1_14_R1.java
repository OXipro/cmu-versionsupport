package com.tomkeuper.spigot.versionsupport;

import org.bukkit.Sound;

class sound_v1_14_R1 extends sound_v1_8_R3 {
    @Override
    public Sound getForCurrentVersion(String v1_8, String v1_12, String v1_13) {
        return getSound(v1_13);
    }
}
