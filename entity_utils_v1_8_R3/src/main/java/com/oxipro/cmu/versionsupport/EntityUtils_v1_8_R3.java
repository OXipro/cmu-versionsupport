package com.oxipro.cmu.versionsupport;

import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public class EntityUtils_v1_8_R3 implements EntityUtilsSupport {
    private static final String PREFIX = "cmu-version-support:";
    private static final int CUSTOM_NAME_LIMIT = 64;

    @Override
    public void setTag(Entity entity, String key, String value) {
        entity.setMetadata(PREFIX + key, new FixedMetadataValue(JavaPlugin.getProvidingPlugin(getClass()), value));
    }

    @Override
    public String getTag(Entity entity, String key) {
        List<MetadataValue> values = entity.getMetadata(PREFIX + key);
        if (values.isEmpty()) return null;
        return values.get(0).asString();
    }

    @Override
    public void setNameTag(Entity entity, Component name) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity) entity;
        String value = toLegacy(name);
        living.setCustomName(value);
        living.setCustomNameVisible(!value.isEmpty());
    }

    @Override
    public void setNameTag(Player viewer, Entity entity, Component name) {
        if (viewer == null || entity == null) {
            setNameTag(entity, name);
            return;
        }
        String truncated = toLegacy(name);
        try {
            DataWatcher watcher = new DataWatcher(null);
            watcher.a(2, truncated);
            watcher.a(3, (byte) (truncated.isEmpty() ? 0 : 1));
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getEntityId(), watcher, true);
            ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packet);
        } catch (RuntimeException ex) {
            setNameTag(entity, name);
        }
    }

    private static String toLegacy(Component name) {
        return truncate(EntityUtilsSupport.toLegacyName(name));
    }

    private static String truncate(String name) {
        if (name == null) {
            return "";
        }
        if (name.length() <= CUSTOM_NAME_LIMIT) {
            return name;
        }
        String cut = name.substring(0, CUSTOM_NAME_LIMIT);
        if (cut.charAt(cut.length() - 1) == '\u00a7') {
            cut = cut.substring(0, cut.length() - 1);
        }
        return cut;
    }
}
