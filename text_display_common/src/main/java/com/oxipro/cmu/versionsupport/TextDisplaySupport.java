package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;

public interface TextDisplaySupport {

    class SupportBuilder {
        private static final String PREFIX = "com.oxipro.cmu.versionsupport.TextDisplaySupport_";

        public static TextDisplaySupport load() {
            String version = VersionMapping.getMCVersion();
            if (!supported(version)) {
                String shown = version != null ? version : Bukkit.getBukkitVersion();
                Bukkit.getLogger().severe("[CMU] Text display requires Minecraft 1.20.6 or newer. This server is " + shown + ".");
                return null;
            }
            TextDisplaySupport loaded = VersionMapping.load(TextDisplaySupport.class, PREFIX);
            if (loaded == null) {
                Bukkit.getLogger().severe("[CMU] Text display is not available for Minecraft " + version + ".");
            }
            return loaded;
        }

        private static boolean supported(String version) {
            if (version == null || version.isEmpty()) {
                return false;
            }
            String[] parts = version.split("\\.");
            try {
                int major = Integer.parseInt(parts[0]);
                int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
                if (major > 1) {
                    return true;
                }
                if (major < 1 || minor < 20) {
                    return false;
                }
                if (minor > 20) {
                    return true;
                }
                return patch >= 6;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
    }
}
