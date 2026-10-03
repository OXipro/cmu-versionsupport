package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    public static String getMCVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        if (bukkitVersion == null || bukkitVersion.isEmpty()) {
            return null;
        }
        return bukkitVersion.split("-")[0];
    }

    @Nullable
    public static String resolveNmsVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String[] parts = packageName.split("\\.");
        if (parts.length >= 4 && parts[3].matches("v\\d+_\\d+_R\\d+")) {
            Bukkit.getLogger().info("[CMU Debug] Resolved NMS version from package: " + parts[3]);
            return parts[3];
        }

        String mcVersion = getMCVersion();
        String nmsVersion = mcVersion == null ? null : VERSION_MAP.get(mcVersion);
        Bukkit.getLogger().info("[CMU Debug] Resolved bukkit version: " + mcVersion);
        Bukkit.getLogger().info("[CMU Debug] Resolved matching bukkit/nms version: " + nmsVersion);
        if (nmsVersion == null) {
            Bukkit.getLogger().severe("[CMU Debug] Unknown server version: " + Bukkit.getBukkitVersion());
        }
        return nmsVersion;
    }

    @Nullable
    public static <T> T load(Class<T> type, String classPrefix, String... fallbackClassNames) {
        String version = resolveNmsVersion();
        if (version == null) {
            return null;
        }

        String[] candidates = new String[2 + fallbackClassNames.length];
        candidates[0] = classPrefix + version;
        int revision = version.lastIndexOf("_R");
        candidates[1] = revision > 0 ? classPrefix + version.substring(0, revision) : null;
        System.arraycopy(fallbackClassNames, 0, candidates, 2, fallbackClassNames.length);
        return instantiate(type, version, candidates);
    }

    @Nullable
    public static <T> T loadNamed(Class<T> type, String... classNames) {
        return instantiate(type, null, classNames);
    }

    @Nullable
    private static <T> T instantiate(Class<T> type, String version, String[] classNames) {
        String label = type.getSimpleName();
        Set<String> seen = new HashSet<String>();
        for (String className : classNames) {
            if (className == null || !seen.add(className)) {
                continue;
            }
            try {
                Bukkit.getLogger().info("[CMU Debug] " + label + " - Trying class: " + className);
                ClassLoader loader = type.getClassLoader();
                Class<?> loaded = Class.forName(className, true, loader == null ? ClassLoader.getSystemClassLoader() : loader);
                Constructor<?> constructor = loaded.getDeclaredConstructor();
                constructor.setAccessible(true);
                T instance = type.cast(constructor.newInstance());
                Bukkit.getLogger().info("[CMU Debug] " + label + " - Successfully loaded: " + loaded.getName());
                return instance;
            } catch (ClassNotFoundException ignored) {
            } catch (ReflectiveOperationException | ClassCastException e) {
                Bukkit.getLogger().severe("[CMU Debug] " + label + " - Failed to instantiate " + className + ": " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        Bukkit.getLogger().severe("[CMU Debug] " + label + " - No suitable class found for: " + (version == null ? "requested name" : version));
        return null;
    }
}
