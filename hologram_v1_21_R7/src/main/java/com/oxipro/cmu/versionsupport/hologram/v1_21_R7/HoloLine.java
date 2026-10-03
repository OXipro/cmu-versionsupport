package com.oxipro.cmu.versionsupport.hologram.v1_21_R7;

import com.mojang.math.Transformation;
import com.oxipro.cmu.versionsupport.HologramSupport;
import com.oxipro.cmu.versionsupport.hologram.IHolo;
import com.oxipro.cmu.versionsupport.hologram.IHoloLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HoloLine implements IHoloLine {
    private static final byte FLAG_SEE_THROUGH = 2;
    private static final int LINE_WIDTH = 2000;

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
        entity = new Display.TextDisplay(EntityTypes.bD, ((CraftWorld) loc.getWorld()).getHandle());
        applyAppearance();
        entity.a_(loc.getX(), loc.getY() + hologram.size() * hologram.getGap(), loc.getZ());

        PacketPlayOutSpawnEntity spawn = spawnPacket();
        PacketPlayOutEntityMetadata metadata = metadataPacket();
        PacketPlayOutEntityTeleport teleport = teleportPacket();

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

        PacketPlayOutEntityMetadata metadata = metadataPacket();
        if (hologram.getAttachedEntity() != null) {
            for (Player player : hologram.getPlayers()) {
                send(player, metadata);
            }
            return;
        }
        PacketPlayOutEntityTeleport teleport = teleportPacket();
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
        PacketPlayOutSpawnEntity spawn = spawnPacket();
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
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.aA());
        for (Player player : hologram.getPlayers()) {
            send(player, packet);
        }
    }

    @Override
    public void remove(Player player) {
        send(player, new PacketPlayOutEntityDestroy(entity.aA()));
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
        return entity.aA();
    }

    private void applyAppearance() {
        IChatBaseComponent nms = toNms(text);
        if (nms == null) {
            nms = CraftChatMessage.fromStringOrEmpty("");
        }
        entity.a(nms);
        entity.a(Display.BillboardConstraints.d);
        entity.aD().a(Display.TextDisplay.aX, LINE_WIDTH);
        entity.aD().a(Display.TextDisplay.aY, 0);
        entity.d(FLAG_SEE_THROUGH);
        float y = 0f;
        if (hologram.getAttachedEntity() != null) {
            int position = hologram.getLines().indexOf(this);
            if (position < 0) {
                position = hologram.size();
            }
            y = (float) (hologram.getAttachOffset() + position * hologram.getGap());
        }
        entity.a(new Transformation(new Vector3f(0f, y, 0f), new Quaternionf(), new Vector3f(1f, 1f, 1f), new Quaternionf()));
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
        entity.a_(loc.getX(), loc.getY() + position * hologram.getGap(), loc.getZ());
    }

    private PacketPlayOutSpawnEntity spawnPacket() {
        return new PacketPlayOutSpawnEntity(
                entity.aA(),
                entity.cY(),
                entity.dP(),
                entity.dR(),
                entity.dV(),
                entity.ee(),
                entity.ec(),
                entity.ay(),
                0,
                entity.dN(),
                entity.cS()
        );
    }

    private PacketPlayOutEntityMetadata metadataPacket() {
        List<DataWatcher.c<?>> metadata = entity.aD().c();
        if (metadata == null) {
            metadata = new ArrayList<>();
        }
        return new PacketPlayOutEntityMetadata(entity.aA(), metadata);
    }

    private PacketPlayOutEntityTeleport teleportPacket() {
        Vec3D delta = new Vec3D(0, 0, 0);
        PositionMoveRotation position = new PositionMoveRotation(entity.dJ(), delta, 0, entity.ee());
        return new PacketPlayOutEntityTeleport(entity.aA(), position, new HashSet<>(), false);
    }

    private static void send(Player player, Packet<?>... packets) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().g;
        for (Packet<?> packet : packets) {
            connection.b(packet);
        }
    }

    private static IChatBaseComponent toNms(Component component) {
        if (component == null || Component.empty().equals(component)) {
            return null;
        }
        return CraftChatMessage.fromJSONOrNull(GsonComponentSerializer.gson().serialize(component));
    }
}
