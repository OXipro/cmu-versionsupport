package com.oxipro.cmu.versionsupport;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerUtils_v1_21_R5 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, @NotNull Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, @NotNull Player receiver, Plugin plugin) {
        receiver.showPlayer(plugin, toUnHide); // ← showPlayer et non hidePlayer
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
        PlayerConnection connection = ((CraftPlayer) player).getHandle().g;
        connection.b(packet);
    }

    @Override
    public void callPlayerDeathEvent(Player player, List<org.bukkit.inventory.ItemStack> drops, int droppedExp, int newLevel, String deathMessage) {
        DamageSource ds = DamageSource.builder(DamageType.GENERIC).build();
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, ds, drops, droppedExp, newLevel, deathMessage);
        Bukkit.getPluginManager().callEvent(deathEvent);
    }
}
