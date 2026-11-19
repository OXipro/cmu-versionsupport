package com.oxipro.cmu.versionsupport;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerUtils_v1_19_R2 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, @NotNull Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, @NotNull Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toUnHide);
    }

    @Override
    public void fakeDamage(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();
        world.playSound(loc, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
        PacketPlayOutAnimation anim = new PacketPlayOutAnimation(((CraftPlayer)player).getHandle(), 1);
        for (Player otherPlayer : world.getPlayers()) {
            sendPacket(otherPlayer, anim);
        }
    }

    public void sendPacket(Player player, Packet<?> packet) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        connection.a(packet);
    }

    @Override
    public void callPlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newLevel, String deathMessage) {
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, drops, droppedExp, newLevel, deathMessage);
        Bukkit.getPluginManager().callEvent(deathEvent);
    }

    @Override
    public ItemStack getOffHandItem(Player player) {
        return player.getInventory().getItemInOffHand();
    }

    @Override
    public boolean isOffHand(org.bukkit.event.Event event) {
        if (event instanceof PlayerInteractEvent) {
            return ((PlayerInteractEvent) event).getHand() == EquipmentSlot.OFF_HAND;
        }
        if (event instanceof PlayerInteractEntityEvent) {
            return ((PlayerInteractEntityEvent) event).getHand() == EquipmentSlot.OFF_HAND;
        }
        return false;
    }


    @Override
    public void setCollide(Player p, boolean v) {
        p.setCollidable(v);
    }

    @Override
    public void setAbsorptionHearts(Player player, double amount) {
        player.setAbsorptionAmount(Math.max(0.0D, amount));
    }

    @Override
    public double getAbsorptionHearts(Player player) {
        if (player == null) {
            return 0.0D;
        }
        return Math.max(0.0D, player.getAbsorptionAmount());
    }


    @Override
    public void hideArmor(Player target, Player viewer, boolean hidden) {
        if (target == null || viewer == null || target.equals(viewer)) {
            return;
        }
        java.util.List<com.mojang.datafixers.util.Pair<net.minecraft.world.entity.EnumItemSlot, net.minecraft.world.item.ItemStack>> slots = new java.util.ArrayList<>();
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EnumItemSlot.c, item(hidden, target.getInventory().getBoots())));
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EnumItemSlot.d, item(hidden, target.getInventory().getLeggings())));
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EnumItemSlot.e, item(hidden, target.getInventory().getChestplate())));
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EnumItemSlot.f, item(hidden, target.getInventory().getHelmet())));
        sendPacket(viewer, new net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment(target.getEntityId(), slots));
    }

    private net.minecraft.world.item.ItemStack item(boolean hidden, org.bukkit.inventory.ItemStack stack) {
        if (hidden || stack == null || stack.getType() == org.bukkit.Material.AIR) {
            return net.minecraft.world.item.ItemStack.b;
        }
        return org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack.asNMSCopy(stack);
    }


}
