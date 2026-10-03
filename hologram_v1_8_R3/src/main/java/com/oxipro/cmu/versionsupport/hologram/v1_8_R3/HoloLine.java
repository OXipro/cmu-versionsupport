package com.oxipro.cmu.versionsupport.hologram.v1_8_R3;

import com.oxipro.cmu.versionsupport.HologramSupport;
import com.oxipro.cmu.versionsupport.hologram.IHolo;
import com.oxipro.cmu.versionsupport.hologram.IHoloLine;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HoloLine implements IHoloLine {
    private static final int CUSTOM_NAME_LIMIT = 64;

    private Component text;
    private IHolo hologram;
    public final EntityArmorStand entity;
    private boolean destroyed = false;

    public HoloLine(String text, IHolo hologram) {
        this(HologramSupport.fromLegacy(text), hologram);
    }

    public HoloLine(Component text, IHolo hologram) {
        this.text = text == null ? Component.empty() : text;
        this.hologram = hologram;
        entity = new EntityArmorStand(((CraftWorld) hologram.getLocation().getWorld()).getHandle(), 0, 0, 0);
        entity.setCustomName(legacyName());
        entity.setCustomNameVisible(true);
        entity.setInvisible(true);
        entity.setGravity(false);
        Location loc = hologram.getLocation();
        entity.setLocation(loc.getX(), loc.getY() + hologram.size() * hologram.getGap(), loc.getZ(), loc.getYaw(), loc.getPitch());

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true);
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(entity);

        for (Player player : hologram.getPlayers()) {
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            pc.sendPacket(packet);
            pc.sendPacket(metadataPacket);
            pc.sendPacket(teleportPacket);
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
        applyState();
        if (destroyed) return;

        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true);
        boolean mounted = hologram.getAttachedEntity() != null;
        PacketPlayOutEntityTeleport packet = mounted ? null : new PacketPlayOutEntityTeleport(entity);

        for (Player player : hologram.getPlayers()) {
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            if (packet != null) {
                pc.sendPacket(packet);
            }
            pc.sendPacket(metadataPacket);
        }
    }

    @Override
    public void update(Player player) {
        if (!hologram.getPlayers().contains(player)) return;
        applyState();
        if (destroyed) return;

        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true);
        PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
        if (hologram.getAttachedEntity() == null) {
            pc.sendPacket(new PacketPlayOutEntityTeleport(entity));
        }
        pc.sendPacket(metadataPacket);
    }

    @Override
    public void reveal() {
        destroyed = false;
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
        for (Player player : hologram.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }

        if (!hologram.getLines().contains(this)) hologram.addLine(this);
        hologram.update();
    }

    @Override
    public void reveal(Player player) {
        destroyed = false;
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

        if (!hologram.getLines().contains(this)) hologram.addLine(this);
        hologram.update(player);
    }

    @Override
    public void remove() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getId());
        for (Player player : hologram.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void remove(Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
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

    private void applyState() {
        entity.setCustomName(legacyName());
        if (hologram.getAttachedEntity() != null) {
            return;
        }
        Location loc = hologram.getLocation();
        int position = hologram.getLines().indexOf(this);
        if (position < 0) {
            position = hologram.size();
        }
        entity.setLocation(loc.getX(), loc.getY() + position * hologram.getGap(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    private String legacyName() {
        String name = HologramSupport.toLegacy(text);
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
