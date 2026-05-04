package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class VersionMapping {

    private static final Map<String, String> VERSION_MAP = new LinkedHashMap<>();

   static {
        VERSION_MAP.put("1.8",     "v1_8_R1");
        VERSION_MAP.put("1.8.3",   "v1_8_R2");
        VERSION_MAP.put("1.8.4",   "v1_8_R3");
        VERSION_MAP.put("1.8.5",   "v1_8_R3");
        VERSION_MAP.put("1.8.6",   "v1_8_R3");
        VERSION_MAP.put("1.8.7",   "v1_8_R3");
        VERSION_MAP.put("1.8.8",   "v1_8_R3");
        // 1.9
        VERSION_MAP.put("1.9",     "v1_9_R1");
        VERSION_MAP.put("1.9.2",   "v1_9_R1");
        VERSION_MAP.put("1.9.4",   "v1_9_R2");
        // 1.10
        VERSION_MAP.put("1.10",    "v1_10_R1");
        VERSION_MAP.put("1.10.2",  "v1_10_R1");
        // 1.11
        VERSION_MAP.put("1.11",    "v1_11_R1");
        VERSION_MAP.put("1.11.1",  "v1_11_R1");
        VERSION_MAP.put("1.11.2",  "v1_11_R1");
        // 1.12
        VERSION_MAP.put("1.12",    "v1_12_R1");
        VERSION_MAP.put("1.12.1",  "v1_12_R1");
        VERSION_MAP.put("1.12.2",  "v1_12_R1");
        // 1.13
        VERSION_MAP.put("1.13",    "v1_13_R1");
        VERSION_MAP.put("1.13.1",  "v1_13_R2");
        VERSION_MAP.put("1.13.2",  "v1_13_R2");
        // 1.14
        VERSION_MAP.put("1.14",    "v1_14_R1");
        VERSION_MAP.put("1.14.1",  "v1_14_R1");
        VERSION_MAP.put("1.14.2",  "v1_14_R1");
        VERSION_MAP.put("1.14.3",  "v1_14_R1");
        VERSION_MAP.put("1.14.4",  "v1_14_R1");
        // 1.15
        VERSION_MAP.put("1.15",    "v1_15_R1");
        VERSION_MAP.put("1.15.1",  "v1_15_R1");
        VERSION_MAP.put("1.15.2",  "v1_15_R1");
        // 1.16
        VERSION_MAP.put("1.16.1",  "v1_16_R1");
        VERSION_MAP.put("1.16.2",  "v1_16_R2");
        VERSION_MAP.put("1.16.3",  "v1_16_R2");
        VERSION_MAP.put("1.16.4",  "v1_16_R3");
        VERSION_MAP.put("1.16.5",  "v1_16_R3");
        // 1.17
        VERSION_MAP.put("1.17",    "v1_17_R1");
        VERSION_MAP.put("1.17.1",  "v1_17_R1");
        // 1.18
        VERSION_MAP.put("1.18",    "v1_18_R1");
        VERSION_MAP.put("1.18.1",  "v1_18_R1");
        VERSION_MAP.put("1.18.2",  "v1_18_R2");
        // 1.19
        VERSION_MAP.put("1.19",    "v1_19_R1");
        VERSION_MAP.put("1.19.1",  "v1_19_R1");
        VERSION_MAP.put("1.19.2",  "v1_19_R1");
        VERSION_MAP.put("1.19.3",  "v1_19_R2");
        VERSION_MAP.put("1.19.4",  "v1_19_R3");
        // 1.20
        VERSION_MAP.put("1.20",    "v1_20_R1");
        VERSION_MAP.put("1.20.1",  "v1_20_R1");
        VERSION_MAP.put("1.20.2",  "v1_20_R2");
        VERSION_MAP.put("1.20.3",  "v1_20_R3");
        VERSION_MAP.put("1.20.4",  "v1_20_R3");
        VERSION_MAP.put("1.20.5",  "v1_20_R4");
        VERSION_MAP.put("1.20.6",  "v1_20_R4");
        // 1.21
        VERSION_MAP.put("1.21",    "v1_21_R1");
        VERSION_MAP.put("1.21.1",  "v1_21_R1");
        VERSION_MAP.put("1.21.2",  "v1_21_R2");
        VERSION_MAP.put("1.21.3",  "v1_21_R2");
        VERSION_MAP.put("1.21.4",  "v1_21_R3");
        VERSION_MAP.put("1.21.5",  "v1_21_R4");
        VERSION_MAP.put("1.21.6",  "v1_21_R5");
        VERSION_MAP.put("1.21.7",  "v1_21_R5");
        VERSION_MAP.put("1.21.8",  "v1_21_R5");
        VERSION_MAP.put("1.21.9",  "v1_21_R6");
        VERSION_MAP.put("1.21.10", "v1_21_R6");
        VERSION_MAP.put("1.21.11", "v1_21_R7");
    }

    @Nullable
    public static String resolveNmsVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String[] parts = packageName.split("\\.");
        if (parts.length >= 4) {
            return parts[3];
        }

        String bukkitVersion = Bukkit.getBukkitVersion().split("-")[0];
        Bukkit.getLogger().info("[CMU Debug] Resolved bukkit version: " + bukkitVersion);
        Bukkit.getLogger().info("[CMU Debug] Resolved bukkit version: " + VERSION_MAP.get(bukkitVersion));
        return VERSION_MAP.get(bukkitVersion);
    }
}