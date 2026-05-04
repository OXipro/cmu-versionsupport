package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

import static com.oxipro.cmu.versionsupport.VersionMapping.resolveNmsVersion;

public interface EntityUtilsSupport {


    void setTag(Entity entity, String key, String value);

    String getTag(Entity entity, String key);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static EntityUtilsSupport load() {
            try {
                String version = resolveNmsVersion();
                Bukkit.getLogger().info("[CMU Debug] Resolved NMS version: " + version);

                if (version == null) {
                    Bukkit.getLogger().severe("[CMU Debug] Unknown server version: " + Bukkit.getBukkitVersion());
                    return null;
                }

                Class<?> c;
                try {
                    String className = "com.oxipro.cmu.versionsupport.EntityUtils_" + version;
                    Bukkit.getLogger().info("[CMU Debug] Trying class: " + className);
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    try {
                        String majorVersion = version.substring(0, version.lastIndexOf("_R"));
                        String className = "com.oxipro.cmu.versionsupport.EntityUtils_" + majorVersion;
                        Bukkit.getLogger().info("[CMU Debug] Trying major class: " + className);
                        c = Class.forName(className);
                    } catch (ClassNotFoundException | StringIndexOutOfBoundsException ex) {
                        Bukkit.getLogger().severe("[CMU Debug] No suitable EntityUtils class found for: " + version);
                        return null;
                    }
                }

                Bukkit.getLogger().info("[CMU Debug] Successfully loaded: " + c.getName());
                return (EntityUtilsSupport) c.getDeclaredConstructor().newInstance();

            } catch (ReflectiveOperationException e) {
                Bukkit.getLogger().severe("[CMU Debug] Failed to instantiate: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
