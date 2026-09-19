package com.oxipro.cmu.versionsupport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

import static com.oxipro.cmu.versionsupport.VersionMapping.resolveNmsVersion;

public interface EntityUtilsSupport {


    void setTag(Entity entity, String key, String value);

    String getTag(Entity entity, String key);

    default void setNameTag(Entity entity, Component name) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity) entity;
        String value = toLegacyName(name);
        living.setCustomName(value);
        living.setCustomNameVisible(!value.isEmpty());
    }

    default void setNameTag(Player viewer, Entity entity, Component name) {
        setNameTag(entity, name);
    }

    static String toLegacyName(Component name) {
        if (name == null || Component.empty().equals(name)) {
            return "";
        }
        return LegacyComponentSerializer.legacySection().serialize(name);
    }

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
