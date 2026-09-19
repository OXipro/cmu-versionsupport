package com.oxipro.cmu.versionsupport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EntityUtils_v1_21_R7 implements EntityUtilsSupport {
    private static final int CUSTOM_NAME_ID = 2;
    private static final int CUSTOM_NAME_VISIBLE_ID = 3;

    @Override
    public void setTag(Entity entity, String key, String value) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey nsKey = new NamespacedKey("cmu-version-support" , key);
        pdc.set(nsKey, PersistentDataType.STRING, value);
    }

    @Override
    public String getTag(Entity entity, String key) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey nsKey = new NamespacedKey("cmu-version-support" , key);
        return pdc.get(nsKey, PersistentDataType.STRING);
    }

    @Override
    public void setNameTag(Entity entity, Component name) {
        if (entity == null) {
            return;
        }
        IChatBaseComponent nms = toNms(name);
        ((CraftEntity) entity).getHandle().b(nms);
        entity.setCustomNameVisible(nms != null);
    }

    @Override
    public void setNameTag(Player viewer, Entity entity, Component name) {
        if (viewer == null || entity == null) {
            setNameTag(entity, name);
            return;
        }
        IChatBaseComponent nms = toNms(name);
        sendNameTag(viewer, entity, nms, nms != null);
    }

    private static void sendNameTag(Player viewer, Entity entity, IChatBaseComponent name, boolean visible) {
        List<DataWatcher.c<?>> items = new ArrayList<>(2);
        items.add(new DataWatcher.c<>(CUSTOM_NAME_ID, DataWatcherRegistry.g, Optional.ofNullable(name)));
        items.add(new DataWatcher.c<>(CUSTOM_NAME_VISIBLE_ID, DataWatcherRegistry.k, visible));
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getEntityId(), items);
        ((CraftPlayer) viewer).getHandle().g.sendPacket(packet);
    }

    private static IChatBaseComponent toNms(Component component) {
        if (component == null || Component.empty().equals(component)) {
            return null;
        }
        return CraftChatMessage.fromJSONOrNull(GsonComponentSerializer.gson().serialize(component));
    }
}
