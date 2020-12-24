package com.andrei1058.spigot.versionsupport;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

@SuppressWarnings("unused")
public class Player_NPC_v1_16_R3 implements PlayerNPCSupport {

    @Override
    public Player spawnNPC(Location location, GameProfile gameProfile) {
        if (location.getWorld() == null){
            throw new IllegalStateException("World is null");
        }
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        EntityPlayer player = new EntityPlayer(((CraftServer) Bukkit.getServer()).getHandle().getServer(), worldServer, gameProfile, new PlayerInteractManager(worldServer));
        player.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(player);
        PacketPlayOutPosition position = new PacketPlayOutPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), new TreeSet<>(), player.getId());

        for (Player inWorld : location.getWorld().getPlayers()) {
            PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().playerConnection;
            connection.sendPacket(spawn);
            connection.sendPacket(position);
        }
        return player.getBukkitEntity();
    }

    @Override
    public Player spawnNPC(Location location, Player playerToCopy, boolean copyArmor) {
        if (location.getWorld() == null){
            throw new IllegalStateException("World is null");
        }
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        EntityPlayer player = new EntityPlayer(((CraftServer) Bukkit.getServer()).getHandle().getServer(), worldServer, ((CraftPlayer) playerToCopy).getProfile(), new PlayerInteractManager(worldServer));
        player.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(player);
        PacketPlayOutPosition position = new PacketPlayOutPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), new TreeSet<>(), player.getId());
        for (Player inWorld : location.getWorld().getPlayers()) {
            PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().playerConnection;
            connection.sendPacket(spawn);
            connection.sendPacket(position);
        }

        if (copyArmor) {
            player.getBukkitEntity().getInventory().setArmorContents(playerToCopy.getInventory().getArmorContents());
            List<Pair<EnumItemSlot, ItemStack>> armor = new ArrayList<>();
            armor.add(Pair.of(EnumItemSlot.HEAD, playerToCopy.getInventory().getHelmet() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getHelmet())));
            armor.add(Pair.of(EnumItemSlot.CHEST, playerToCopy.getInventory().getChestplate() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getChestplate())));
            armor.add(Pair.of(EnumItemSlot.LEGS, playerToCopy.getInventory().getLeggings() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getLeggings())));
            armor.add(Pair.of(EnumItemSlot.FEET, playerToCopy.getInventory().getBoots() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getBoots())));
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getId(), armor);
            for (Player inWorld : location.getWorld().getPlayers()) {
                PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().playerConnection;
                connection.sendPacket(packet);
            }
        }
        return player.getBukkitEntity();
    }


    @Override
    public void sendDestroyPacket(org.bukkit.entity.Entity entity, Player receiver) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        ((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void sendDestroyPacket(org.bukkit.entity.Entity entity, List<Player> receivers) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        for (Player receiver : receivers) {
            ((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void sendDestroyPacket(org.bukkit.entity.Entity entity, Collection<? extends Player> receivers) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        for (Player receiver : receivers) {
            ((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
