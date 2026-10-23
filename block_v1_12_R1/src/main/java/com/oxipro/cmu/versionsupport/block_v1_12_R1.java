package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.lang.reflect.Field;

class block_v1_12_R1 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        //noinspection deprecation
        block.setData(data, true);
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        try {
            net.minecraft.server.v1_12_R1.Block block = net.minecraft.server.v1_12_R1.Block.getByName(oldName);
            if (block == null) {
                Bukkit.getLogger().warning("[CMU] Unknown block for hardness: " + oldName);
                return;
            }
            Field field = net.minecraft.server.v1_12_R1.Block.class.getDeclaredField("strength");
            field.setAccessible(true);
            field.setFloat(block, durability);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Bukkit.getLogger().warning("[CMU] Failed to set block hardness for " + oldName + ": " + e.getMessage());
        }
    }

    public Block getBlockBehindSign(Block sign) {
        BlockState block = sign.getState();
        if (!(block instanceof org.bukkit.material.Sign)) return null;
        return block.getBlock().getRelative(((org.bukkit.material.Sign) block.getData()).getAttachedFace());
    }

    public void sendBlockBreakAnimation(org.bukkit.entity.Player player, org.bukkit.block.Block block, int value) {
        if (player == null || block == null) {
            return;
        }
        net.minecraft.server.v1_12_R1.BlockPosition position = new net.minecraft.server.v1_12_R1.BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation packet = new net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation(player.getEntityId(), position, value);
        net.minecraft.server.v1_12_R1.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player).getHandle().playerConnection;
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }
}
