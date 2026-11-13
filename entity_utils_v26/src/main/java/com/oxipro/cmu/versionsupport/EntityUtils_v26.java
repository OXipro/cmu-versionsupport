package com.oxipro.cmu.versionsupport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityUtils_v26 implements EntityUtilsSupport {
    private static final int CUSTOM_NAME_ID = 2;
    private static final int CUSTOM_NAME_VISIBLE_ID = 3;

    @Override
    public void setTag(Entity entity, String key, String value) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey nsKey = new NamespacedKey("cmu-version-support", key);
        pdc.set(nsKey, PersistentDataType.STRING, value);
    }

    @Override
    public String getTag(Entity entity, String key) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey nsKey = new NamespacedKey("cmu-version-support", key);
        return pdc.get(nsKey, PersistentDataType.STRING);
    }

    @Override
    public void setNameTag(Entity entity, Component name) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity living = (LivingEntity) entity;
        String value = EntityUtilsSupport.toLegacyName(name);
        living.setCustomName(value);
        living.setCustomNameVisible(!value.isEmpty());
    }

    @Override
    public void setNameTag(Player viewer, Entity entity, Component name) {
        if (viewer == null || entity == null) {
            setNameTag(entity, name);
            return;
        }
        net.minecraft.network.chat.Component nms = toNms(name);
        try {
            sendNameTag(viewer, entity, nms, nms != null);
        } catch (RuntimeException ex) {
            setNameTag(entity, name);
        }
    }

    private static void sendNameTag(Player viewer, Entity entity, net.minecraft.network.chat.Component name, boolean visible) {
        List<SynchedEntityData.DataValue<?>> items = new ArrayList<SynchedEntityData.DataValue<?>>(2);
        items.add(new SynchedEntityData.DataValue<Optional<net.minecraft.network.chat.Component>>(CUSTOM_NAME_ID, EntityDataSerializers.OPTIONAL_COMPONENT, Optional.ofNullable(name)));
        items.add(new SynchedEntityData.DataValue<Boolean>(CUSTOM_NAME_VISIBLE_ID, EntityDataSerializers.BOOLEAN, visible));
        ClientboundSetEntityDataPacket packet = new ClientboundSetEntityDataPacket(entity.getEntityId(), items);
        ((CraftPlayer) viewer).getHandle().connection.send(packet);
    }

    private static net.minecraft.network.chat.Component toNms(Component component) {
        if (component == null || Component.empty().equals(component)) {
            return null;
        }
        return CraftChatMessage.fromJSONOrNull(GsonComponentSerializer.gson().serialize(component));
    }
}
