package com.andrei1058.spigot.versionsupport;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings("unused")
public class Player_NPC_v1_18_R1 implements PlayerNPCSupport {

    @Override
    public Player spawnNPC(Location location, GameProfile gameProfile) {
        if (location.getWorld() == null){
            throw new IllegalStateException("World is null");
        }
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        EntityPlayer player = new EntityPlayer(((CraftServer) Bukkit.getServer()).getHandle().b(), worldServer, gameProfile);
        player.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(player);
        PacketPlayOutPosition position = new PacketPlayOutPosition(
                location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(),
                PacketPlayOutPosition.EnumPlayerTeleportFlags.a(0), player.getBukkitEntity().getEntityId(), false
        );

        for (Player inWorld : location.getWorld().getPlayers()) {
            PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().b;
            connection.a(spawn);
            connection.a(position);
        }
        return player.getBukkitEntity();
    }

    @Override
    public Player spawnNPC(Location location, Player playerToCopy, boolean copyArmor) {
        if (location.getWorld() == null){
            throw new IllegalStateException("World is null");
        }
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        EntityPlayer player = new EntityPlayer(
                ((CraftServer) Bukkit.getServer()).getHandle().b(), worldServer,
                ((CraftPlayer) playerToCopy).getProfile()
        );
        player.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(player);
        PacketPlayOutPosition position = new PacketPlayOutPosition(
                location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(),
                PacketPlayOutPosition.EnumPlayerTeleportFlags.a(0), player.getBukkitEntity().getEntityId(), false
        );
        for (Player inWorld : location.getWorld().getPlayers()) {
            PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().b;
            connection.a(spawn);
            connection.a(position);
        }

        if (copyArmor) {
            player.getBukkitEntity().getInventory().setArmorContents(playerToCopy.getInventory().getArmorContents());
            List<Pair<EnumItemSlot, ItemStack>> armor = new ArrayList<>();
            armor.add(Pair.of(EnumItemSlot.f, playerToCopy.getInventory().getHelmet() == null ? new ItemStack(Item.b(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getHelmet())));
            armor.add(Pair.of(EnumItemSlot.e, playerToCopy.getInventory().getChestplate() == null ? new ItemStack(Item.b(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getChestplate())));
            armor.add(Pair.of(EnumItemSlot.d, playerToCopy.getInventory().getLeggings() == null ? new ItemStack(Item.b(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getLeggings())));
            armor.add(Pair.of(EnumItemSlot.c, playerToCopy.getInventory().getBoots() == null ? new ItemStack(Item.b(0)) : CraftItemStack.asNMSCopy(playerToCopy.getInventory().getBoots())));
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getBukkitEntity().getEntityId(), armor);
            for (Player inWorld : location.getWorld().getPlayers()) {
                PlayerConnection connection = ((CraftPlayer) inWorld).getHandle().b;
                connection.a(packet);
            }
        }
        return player.getBukkitEntity();
    }


    @Override
    public void sendDestroyPacket(org.bukkit.entity.Entity entity, Player receiver) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        ((CraftPlayer) receiver).getHandle().b.a(packet);
    }

    @Override
    public void sendDestroyPacket(org.bukkit.entity.Entity entity, List<Player> receivers) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        for (Player receiver : receivers) {
            ((CraftPlayer) receiver).getHandle().b.a(packet);
        }
    }

    @Override
    public void sendDestroyPacket(org.bukkit.entity.Entity entity, Collection<? extends Player> receivers) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getEntityId());
        for (Player receiver : receivers) {
            ((CraftPlayer) receiver).getHandle().b.a(packet);
        }
    }
}
