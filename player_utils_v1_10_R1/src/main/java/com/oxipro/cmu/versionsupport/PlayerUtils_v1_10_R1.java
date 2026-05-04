package com.oxipro.cmu.versionsupport;

import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerUtils_v1_10_R1 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, Player receiver, Plugin plugin) {
        receiver.hidePlayer(toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, Player receiver, Plugin plugin) {
        receiver.hidePlayer(toUnHide);
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
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packet);
    }

    @Override
    public void callPlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newLevel, String deathMessage) {
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, drops, droppedExp, newLevel, deathMessage);
        Bukkit.getPluginManager().callEvent(deathEvent);
    }
}
