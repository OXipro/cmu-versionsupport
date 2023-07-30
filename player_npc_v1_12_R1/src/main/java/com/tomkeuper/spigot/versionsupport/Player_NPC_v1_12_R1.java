package com.tomkeuper.spigot.versionsupport;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

@SuppressWarnings("unused")
public class Player_NPC_v1_12_R1 implements PlayerNPCSupport {

    @Override
    public Player spawnNPC(Location location, GameProfile gameProfile) {
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
            player.getBukkitEntity().getEquipment().setArmorContents(playerToCopy.getEquipment().getArmorContents());
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
