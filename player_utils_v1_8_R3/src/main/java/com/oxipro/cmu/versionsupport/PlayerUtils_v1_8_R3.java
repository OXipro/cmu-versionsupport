package com.oxipro.cmu.versionsupport;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerUtils_v1_8_R3 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, Player receiver, Plugin plugin) {
        receiver.hidePlayer(toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, Player receiver, Plugin plugin) {
        receiver.showPlayer(toUnHide);
    }

    @Override
    public void fakeDamage(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();
        world.playSound(loc, Sound.HURT_FLESH, 1.0f, 1.0f);
        PacketPlayOutAnimation anim = new PacketPlayOutAnimation(((CraftPlayer)player).getHandle(), 1);
        for (Player otherPlayer : world.getPlayers()) {
            sendPacket(otherPlayer, anim);
        }
    }

    public void sendPacket(Player player, net.minecraft.server.v1_8_R3.Packet<?> packet) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packet);
    }

    @Override
    public void callPlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newLevel, String deathMessage) {
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, drops, droppedExp, newLevel, deathMessage);
        Bukkit.getPluginManager().callEvent(deathEvent);
    }

    @Override
    public ItemStack getOffHandItem(Player player) {
        return null;
    }

    @Override
    public void setCollide(Player p, boolean v) {
        p.spigot().setCollidesWithEntities(v);
    }

    @Override
    public void setAbsorptionHearts(Player player, double amount) {
        ((CraftPlayer) player).getHandle().setAbsorptionHearts((float) Math.max(0.0D, amount));

        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        nmsPlayer.getDataWatcher().watch(9, (byte) 0);

        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(
                nmsPlayer.getId(),
                nmsPlayer.getDataWatcher(),
                true
        );

        for (org.bukkit.entity.Player viewer : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public double getAbsorptionHearts(Player player) {
        if (player == null) {
            return 0.0D;
        }
        return Math.max(0.0F, ((CraftPlayer) player).getHandle().getAbsorptionHearts());
    }
}
