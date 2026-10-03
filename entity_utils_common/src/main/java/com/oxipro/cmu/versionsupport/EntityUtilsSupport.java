package com.oxipro.cmu.versionsupport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

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
            return VersionMapping.load(EntityUtilsSupport.class, "com.oxipro.cmu.versionsupport.EntityUtils_");
        }
    }
}
