package com.andrei1058.spigot.versionsupport;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.TreeSet;

public class Player_NPC_1_13_R2 implements PlayerNPCSupport {

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
            PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(player.getId(), EnumItemSlot.HEAD, playerToCopy.getInventory().getHelmet() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getHelmet()));
            PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(player.getId(), EnumItemSlot.CHEST, playerToCopy.getInventory().getChestplate() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getChestplate()));
            PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(player.getId(), EnumItemSlot.LEGS, playerToCopy.getInventory().getLeggings() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getLeggings()));
            PacketPlayOutEntityEquipment feet = new PacketPlayOutEntityEquipment(player.getId(), EnumItemSlot.FEET, playerToCopy.getInventory().getBoots() == null ? new ItemStack(Item.getById(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getBoots()));
            for (Player inWorld : location.getWorld().getPlayers()) {
                PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().playerConnection;
                connection.sendPacket(head);
                connection.sendPacket(chest);
                connection.sendPacket(leggings);
                connection.sendPacket(feet);
            }
        }
        return player.getBukkitEntity();
    }
}