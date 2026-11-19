package com.oxipro.cmu.versionsupport;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerUtils_v26 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, @NotNull Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, @NotNull Player receiver, Plugin plugin) {
        receiver.showPlayer(plugin, toUnHide);
    }

    @Override
    public void fakeDamage(Player player) {
        Location loc = player.getLocation();
        World world = player.getWorld();
        world.playSound(loc, Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
        ClientboundAnimatePacket anim = new ClientboundAnimatePacket(((CraftPlayer) player).getHandle(), 1);
        for (Player otherPlayer : world.getPlayers()) {
            sendPacket(otherPlayer, anim);
        }
    }

    public void sendPacket(Player player, Packet<?> packet) {
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(packet);
    }

    @Override
    public void callPlayerDeathEvent(Player player, List<org.bukkit.inventory.ItemStack> drops, int droppedExp, int newLevel, String deathMessage) {
        DamageSource ds = DamageSource.builder(DamageType.GENERIC).build();
        PlayerDeathEvent deathEvent = new PlayerDeathEvent(player, ds, drops, droppedExp, newLevel, deathMessage);
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
        double value = Math.max(0.0D, amount);
        AttributeInstance maxAbsorption = player.getAttribute(Attribute.MAX_ABSORPTION);
        if (maxAbsorption != null) {
            maxAbsorption.setBaseValue(value);
        }
        player.setAbsorptionAmount(value);
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
        net.minecraft.world.entity.Entity handle = ((org.bukkit.craftbukkit.entity.CraftEntity) target).getHandle();
        net.minecraft.network.syncher.EntityDataAccessor<Byte> flags = new net.minecraft.network.syncher.EntityDataAccessor<>(0, net.minecraft.network.syncher.EntityDataSerializers.BYTE);
        byte value = handle.getEntityData().get(flags);
        value = enabled ? (byte) (value | 0x40) : (byte) (value & ~0x40);
        java.util.List<net.minecraft.network.syncher.SynchedEntityData.DataValue<?>> entries = new java.util.ArrayList<>();
        entries.add(net.minecraft.network.syncher.SynchedEntityData.DataValue.create(flags, value));
        sendPacket(viewer, new net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket(handle.getId(), entries));
    }

    @Override
    public void hideArmor(Player target, Player viewer, boolean hidden) {
        if (target == null || viewer == null || target.equals(viewer)) {
            return;
        }
        java.util.List<com.mojang.datafixers.util.Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> slots = new java.util.ArrayList<>();
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EquipmentSlot.FEET, item(hidden, target.getInventory().getBoots())));
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EquipmentSlot.LEGS, item(hidden, target.getInventory().getLeggings())));
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EquipmentSlot.CHEST, item(hidden, target.getInventory().getChestplate())));
        slots.add(new com.mojang.datafixers.util.Pair<>(net.minecraft.world.entity.EquipmentSlot.HEAD, item(hidden, target.getInventory().getHelmet())));
        sendPacket(viewer, new net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket(target.getEntityId(), slots));
    }

    private net.minecraft.world.item.ItemStack item(boolean hidden, org.bukkit.inventory.ItemStack stack) {
        if (hidden || stack == null || stack.getType() == org.bukkit.Material.AIR) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        return org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(stack);
    }


    private void syncGlowTeam(Player viewer, org.bukkit.entity.Entity target, GlowColor color, boolean enabled) {
        String entry = GlowTeams.entry(target);
        String previous = GlowTeams.take(viewer.getUniqueId(), target.getEntityId());
        if (previous != null) {
            net.minecraft.world.scores.Scoreboard removing = new net.minecraft.world.scores.Scoreboard();
            net.minecraft.world.scores.PlayerTeam old = removing.addPlayerTeam(previous);
            sendPacket(viewer, net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket.createRemovePacket(old));
        }
        if (enabled && color != null) {
            String name = GlowTeams.name(target.getEntityId(), color);
            net.minecraft.world.scores.Scoreboard board = new net.minecraft.world.scores.Scoreboard();
            net.minecraft.world.scores.PlayerTeam team = board.addPlayerTeam(name);
            team.setAllowFriendlyFire(true);
            team.setNameTagVisibility(net.minecraft.world.scores.Team.Visibility.ALWAYS);
            team.setCollisionRule(net.minecraft.world.scores.Team.CollisionRule.ALWAYS);
            team.setColor(net.minecraft.ChatFormatting.values()[color.id()]);
            board.addPlayerToTeam(entry, team);
            sendPacket(viewer, net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));
            GlowTeams.keep(viewer.getUniqueId(), target.getEntityId(), name);
        } else if (previous != null) {
            net.minecraft.world.scores.Scoreboard scoreboard = ((org.bukkit.craftbukkit.scoreboard.CraftScoreboard) viewer.getScoreboard()).getHandle();
            net.minecraft.world.scores.PlayerTeam real = scoreboard.getPlayersTeam(entry);
            if (real != null) {
                sendPacket(viewer, net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket.createPlayerPacket(real, entry, net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket.Action.ADD));
            }
        }
    }
}
