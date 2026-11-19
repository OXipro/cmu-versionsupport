package com.oxipro.cmu.versionsupport;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_14_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerUtils_v1_14_R1 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, Player receiver, Plugin plugin) {
        receiver.showPlayer(plugin, toUnHide);
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
    public void glow(org.bukkit.entity.Entity target, Player viewer, GlowColor color, boolean enabled) {
        if (target == null || viewer == null) {
            return;
        }
        syncGlowTeam(viewer, target, color, enabled);
        net.minecraft.server.v1_14_R1.Entity handle = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity) target).getHandle();
        net.minecraft.server.v1_14_R1.DataWatcherObject<Byte> flags = new net.minecraft.server.v1_14_R1.DataWatcherObject<>(0, net.minecraft.server.v1_14_R1.DataWatcherRegistry.a);
        byte value = handle.getDataWatcher().get(flags);
        value = enabled ? (byte) (value | 0x40) : (byte) (value & ~0x40);
        net.minecraft.server.v1_14_R1.DataWatcher watcher = new net.minecraft.server.v1_14_R1.DataWatcher(handle);
        watcher.register(flags, value);
        sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutEntityMetadata(handle.getId(), watcher, true));
    }

    @Override
    public void hideArmor(Player target, Player viewer, boolean hidden) {
        if (target == null || viewer == null || target.equals(viewer)) {
            return;
        }
        int id = target.getEntityId();
        sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutEntityEquipment(id, net.minecraft.server.v1_14_R1.EnumItemSlot.FEET, item(hidden, target.getInventory().getBoots())));
        sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutEntityEquipment(id, net.minecraft.server.v1_14_R1.EnumItemSlot.LEGS, item(hidden, target.getInventory().getLeggings())));
        sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutEntityEquipment(id, net.minecraft.server.v1_14_R1.EnumItemSlot.CHEST, item(hidden, target.getInventory().getChestplate())));
        sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutEntityEquipment(id, net.minecraft.server.v1_14_R1.EnumItemSlot.HEAD, item(hidden, target.getInventory().getHelmet())));
    }

    private net.minecraft.server.v1_14_R1.ItemStack item(boolean hidden, org.bukkit.inventory.ItemStack stack) {
        if (hidden || stack == null || stack.getType() == org.bukkit.Material.AIR) {
            return net.minecraft.server.v1_14_R1.ItemStack.a;
        }
        return org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack.asNMSCopy(stack);
    }


    private void syncGlowTeam(Player viewer, org.bukkit.entity.Entity target, GlowColor color, boolean enabled) {
        String entry = GlowTeams.entry(target);
        String previous = GlowTeams.take(viewer.getUniqueId(), target.getEntityId());
        if (previous != null) {
            net.minecraft.server.v1_14_R1.Scoreboard removing = new net.minecraft.server.v1_14_R1.Scoreboard();
            net.minecraft.server.v1_14_R1.ScoreboardTeam old = removing.createTeam(previous);
            sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutScoreboardTeam(old, 1));
        }
        if (enabled && color != null) {
            String name = GlowTeams.name(target.getEntityId(), color);
            net.minecraft.server.v1_14_R1.Scoreboard board = new net.minecraft.server.v1_14_R1.Scoreboard();
            net.minecraft.server.v1_14_R1.ScoreboardTeam team = board.createTeam(name);
            team.setAllowFriendlyFire(true);
            team.setNameTagVisibility(net.minecraft.server.v1_14_R1.ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
            team.setCollisionRule(net.minecraft.server.v1_14_R1.ScoreboardTeamBase.EnumTeamPush.ALWAYS);
            team.setColor(net.minecraft.server.v1_14_R1.EnumChatFormat.a(color.id()));
            board.addPlayerToTeam(entry, team);
            sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutScoreboardTeam(team, 0));
            GlowTeams.keep(viewer.getUniqueId(), target.getEntityId(), name);
        } else if (previous != null) {
            net.minecraft.server.v1_14_R1.Scoreboard scoreboard = ((org.bukkit.craftbukkit.v1_14_R1.scoreboard.CraftScoreboard) viewer.getScoreboard()).getHandle();
            net.minecraft.server.v1_14_R1.ScoreboardTeam real = scoreboard.getPlayerTeam(entry);
            if (real != null) {
                sendPacket(viewer, new net.minecraft.server.v1_14_R1.PacketPlayOutScoreboardTeam(real, java.util.Collections.singleton(entry), 3));
            }
        }
    }
}
