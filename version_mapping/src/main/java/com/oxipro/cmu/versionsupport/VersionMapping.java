package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VersionMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionMapping.class);

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

    static Map<String, String> versions() {
        return Collections.unmodifiableMap(VERSION_MAP);
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
        String nmsVersion = resolve(packageName, Bukkit.getBukkitVersion(), VERSION_MAP);
        if (nmsVersion == null) {
            LOGGER.error("Unknown server version: {}", Bukkit.getBukkitVersion());
        } else {
            LOGGER.debug("Resolved NMS version: {}", nmsVersion);
        }
        return nmsVersion;
    }

    @Nullable
    static String resolve(String packageName, String bukkitVersion, Map<String, String> versionMap) {
        if (packageName != null) {
            String[] parts = packageName.split("\\.");
            if (parts.length >= 4 && parts[3].matches("v\\d+_\\d+_R\\d+")) {
                return parts[3];
            }
        }
        String mcVersion = minecraftVersion(bukkitVersion);
        if (mcVersion == null) {
            return null;
        }
        String mapped = versionMap == null ? null : versionMap.get(mcVersion);
        if (mapped != null) {
            return mapped;
        }
        return yearToken(mcVersion);
    }

    @Nullable
    static String minecraftVersion(String bukkitVersion) {
        if (bukkitVersion == null || bukkitVersion.isEmpty()) {
            return null;
        }
        String version = bukkitVersion.split("-")[0];
        return version.isEmpty() ? null : version;
    }

    @Nullable
    static String yearToken(String mcVersion) {
        if (mcVersion == null || !mcVersion.matches("\\d+\\.\\d+(\\.\\d+)?")) {
            return null;
        }
        String[] parts = mcVersion.split("\\.");
        int major;
        try {
            major = Integer.parseInt(parts[0]);
        } catch (NumberFormatException ex) {
            return null;
        }
        if (major < 26) {
            return null;
        }
        String minor = parts[1];
        String patch = parts.length > 2 ? parts[2] : null;
        if (patch != null && !"0".equals(patch)) {
            return "v" + major + "_" + minor + "_" + patch;
        }
        if ("1".equals(minor)) {
            return "v" + major;
        }
        return "v" + major + "_" + minor;
    }

    static List<String> versionClassNames(String classPrefix, String version) {
        List<String> names = new ArrayList<String>();
        if (version == null || version.isEmpty()) {
            return names;
        }
        names.add(classPrefix + version);
        int revision = version.lastIndexOf("_R");
        if (revision > 0) {
            names.add(classPrefix + version.substring(0, revision));
            return names;
        }
        if (version.matches("v\\d+_\\d+_\\d+")) {
            String minor = version.substring(0, version.lastIndexOf('_'));
            names.add(classPrefix + minor);
            names.add(classPrefix + minor.substring(0, minor.lastIndexOf('_')));
            return names;
        }
        if (version.matches("v\\d+_\\d+")) {
            int split = version.lastIndexOf('_');
            int major = Integer.parseInt(version.substring(1, split));
            if (major >= 26) {
                names.add(classPrefix + version.substring(0, split));
            }
        }
        return names;
    }

    @Nullable
    public static <T> T load(Class<T> type, String classPrefix, String... fallbackClassNames) {
        String version = resolveNmsVersion();
        if (version == null) {
            return null;
        }

        List<String> candidates = new ArrayList<String>(versionClassNames(classPrefix, version));
        for (String fallback : fallbackClassNames) {
            candidates.add(fallback);
        }
        return instantiate(type, version, candidates.toArray(new String[candidates.size()]));
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
                LOGGER.trace("{} - Trying class: {}", label, className);
                ClassLoader loader = type.getClassLoader();
                Class<?> loaded = Class.forName(className, true, loader == null ? ClassLoader.getSystemClassLoader() : loader);
                Constructor<?> constructor = loaded.getDeclaredConstructor();
                constructor.setAccessible(true);
                T instance = type.cast(constructor.newInstance());
                LOGGER.debug("Loaded {} for {}", loaded.getName(), label);
                return instance;
            } catch (ClassNotFoundException ignored) {
                LOGGER.trace("{} - Class not found: {}", label, className);
            } catch (ReflectiveOperationException | ClassCastException e) {
                LOGGER.error("{} - Failed to instantiate {}", label, className, e);
                return null;
            }
        }
        LOGGER.error("{} - No suitable class found for: {}", label, version == null ? "requested name" : version);
        return null;
    }
}
