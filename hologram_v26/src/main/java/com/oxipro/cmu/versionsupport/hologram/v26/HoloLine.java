package com.oxipro.cmu.versionsupport.hologram.v26;

import com.mojang.math.Transformation;
import com.oxipro.cmu.versionsupport.HologramSupport;
import com.oxipro.cmu.versionsupport.hologram.IHolo;
import com.oxipro.cmu.versionsupport.hologram.IHoloLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PositionMoveRotation;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HoloLine implements IHoloLine {
    private static final int LINE_WIDTH = 2000;
    private static final int BACKGROUND = 1073741824;
    private static final EntityType<?> TEXT_DISPLAY = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace("text_display"));

    private Component text;
    private IHolo hologram;
    public final Display.TextDisplay entity;
    private boolean destroyed = false;

    public HoloLine(String text, IHolo hologram) {
        this(HologramSupport.fromLegacy(text), hologram);
    }

    public HoloLine(Component text, IHolo hologram) {
        this.text = text == null ? Component.empty() : text;
        this.hologram = hologram;
        Location loc = hologram.getLocation();
        entity = new Display.TextDisplay(TEXT_DISPLAY, ((CraftWorld) loc.getWorld()).getHandle());
        applyAppearance();
        entity.setPos(loc.getX(), loc.getY() + hologram.size() * hologram.getGap(), loc.getZ());

        ClientboundAddEntityPacket spawn = spawnPacket();
        ClientboundSetEntityDataPacket metadata = metadataPacket();
        ClientboundTeleportEntityPacket teleport = teleportPacket();

        for (Player player : hologram.getPlayers()) {
            send(player, spawn, metadata, teleport);
        }
    }

    @Override
    public void setText(String text) {
        setText(HologramSupport.fromLegacy(text), true);
    }

    @Override
    public void setText(String text, boolean update) {
        setText(HologramSupport.fromLegacy(text), update);
    }

    @Override
    public void setText(Component text) {
        setText(text, true);
    }

    @Override
    public void setText(Component text, boolean update) {
        this.text = text == null ? Component.empty() : text;
        if (update) {
            update();
        }
    }

    @Override
    public String getText() {
        return HologramSupport.toLegacy(text);
    }

    @Override
    public Component getComponent() {
        return text;
    }

    @Override
    public void setHologram(IHolo hologram) {
        this.hologram = hologram;
    }

    @Override
    public IHolo getHologram() {
        return hologram;
    }

    @Override
    public void update() {
        applyAppearance();
        applyPosition();
        if (isDestroyed()) return;

        ClientboundSetEntityDataPacket metadata = metadataPacket();
        if (hologram.getAttachedEntity() != null) {
            for (Player player : hologram.getPlayers()) {
                send(player, metadata);
            }
            return;
        }
        ClientboundTeleportEntityPacket teleport = teleportPacket();
        for (Player player : hologram.getPlayers()) {
            send(player, metadata, teleport);
        }
    }

    @Override
    public void update(Player player) {
        if (!hologram.getPlayers().contains(player)) return;
        applyAppearance();
        applyPosition();
        if (isDestroyed()) return;

        if (hologram.getAttachedEntity() != null) {
            send(player, metadataPacket());
            return;
        }
        send(player, metadataPacket(), teleportPacket());
    }

    @Override
    public void reveal() {
        destroyed = false;
        ClientboundAddEntityPacket spawn = spawnPacket();
        for (Player player : hologram.getPlayers()) {
            send(player, spawn);
        }

        if (!hologram.getLines().contains(this)) hologram.addLine(this);
        hologram.update();
    }

    @Override
    public void reveal(Player player) {
        destroyed = false;
        send(player, spawnPacket());
        if (!hologram.getLines().contains(this)) hologram.addLine(this);
        hologram.update(player);
    }

    @Override
    public void remove() {
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(entity.getId());
        for (Player player : hologram.getPlayers()) {
            send(player, packet);
        }
    }

    @Override
    public void remove(Player player) {
        send(player, new ClientboundRemoveEntitiesPacket(entity.getId()));
    }

    @Override
    public void destroy() {
        destroyed = true;
        remove();
        hologram.removeLine(this);
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public int getEntityId() {
        return entity.getId();
    }

    private void applyAppearance() {
        net.minecraft.network.chat.Component nms = toNms(text);
        if (nms == null) {
            nms = CraftChatMessage.fromStringOrEmpty("");
        }
        entity.setText(nms);
        entity.setBillboardConstraints(Display.BillboardConstraints.CENTER);
        entity.getEntityData().set(Display.TextDisplay.DATA_LINE_WIDTH_ID, LINE_WIDTH);
        entity.getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, BACKGROUND);
        float y = 0f;
        if (hologram.getAttachedEntity() != null) {
            int position = hologram.getLines().indexOf(this);
            if (position < 0) {
                position = hologram.size();
            }
            y = (float) (hologram.getAttachOffset() + position * hologram.getGap());
        }
        entity.setTransformation(new Transformation(new Vector3f(0f, y, 0f), new Quaternionf(), new Vector3f(1f, 1f, 1f), new Quaternionf()));
    }

    private void applyPosition() {
        if (hologram.getAttachedEntity() != null) {
            return;
        }
        Location loc = hologram.getLocation();
        int position = hologram.getLines().indexOf(this);
        if (position < 0) {
            position = hologram.size();
        }
        entity.setPos(loc.getX(), loc.getY() + position * hologram.getGap(), loc.getZ());
    }

    private ClientboundAddEntityPacket spawnPacket() {
        return new ClientboundAddEntityPacket(
                entity.getId(),
                entity.getUUID(),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                entity.getXRot(),
                entity.getYRot(),
                entity.getType(),
                0,
                entity.getDeltaMovement(),
                entity.getYHeadRot()
        );
    }

    private ClientboundSetEntityDataPacket metadataPacket() {
        List<SynchedEntityData.DataValue<?>> metadata = entity.getEntityData().getNonDefaultValues();
        if (metadata == null) {
            metadata = new ArrayList<SynchedEntityData.DataValue<?>>();
        }
        return new ClientboundSetEntityDataPacket(entity.getId(), metadata);
    }

    private ClientboundTeleportEntityPacket teleportPacket() {
        return new ClientboundTeleportEntityPacket(entity.getId(), PositionMoveRotation.of(entity), Set.of(), false);
    }

    private static void send(Player player, Packet<?>... packets) {
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        for (Packet<?> packet : packets) {
            connection.send(packet);
        }
    }

    private static net.minecraft.network.chat.Component toNms(Component component) {
        if (component == null || Component.empty().equals(component)) {
            return null;
        }
        return CraftChatMessage.fromJSONOrNull(GsonComponentSerializer.gson().serialize(component));
    }
}
