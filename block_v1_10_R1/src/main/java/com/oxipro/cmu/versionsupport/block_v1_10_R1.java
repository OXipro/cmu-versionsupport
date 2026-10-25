package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.lang.reflect.Field;

class block_v1_10_R1 implements BlockSupport {
    public void setBlockData(Block block, byte data) {
        //noinspection deprecation
        block.setData(data, true);
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        try {
            net.minecraft.server.v1_10_R1.Block block = net.minecraft.server.v1_10_R1.Block.getByName(oldName);
            if (block == null) {
                Bukkit.getLogger().warning("[CMU] Unknown block for hardness: " + oldName);
                return;
            }
            Field field = net.minecraft.server.v1_10_R1.Block.class.getDeclaredField("strength");
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

    public float getDurability(org.bukkit.block.Block block) {
        if (block == null) {
            return 0.0F;
        }
        try {
            net.minecraft.server.v1_10_R1.World world = ((org.bukkit.craftbukkit.v1_10_R1.CraftWorld) block.getWorld()).getHandle();
            net.minecraft.server.v1_10_R1.BlockPosition position = new net.minecraft.server.v1_10_R1.BlockPosition(block.getX(), block.getY(), block.getZ());
            net.minecraft.server.v1_10_R1.Block type = world.getType(position).getBlock();
            java.lang.reflect.Field field = net.minecraft.server.v1_10_R1.Block.class.getDeclaredField("strength");
            field.setAccessible(true);
            return field.getFloat(type);
        } catch (ReflectiveOperationException ex) {
            org.bukkit.Bukkit.getLogger().warning("[CMU] Failed to read block hardness: " + ex.getMessage());
            return 0.0F;
        }
    }

    public void sendBlockBreakAnimation(org.bukkit.entity.Player player, int sourceId, org.bukkit.block.Block block, int value) {
        if (player == null || block == null) {
            return;
        }
        net.minecraft.server.v1_10_R1.BlockPosition position = new net.minecraft.server.v1_10_R1.BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.server.v1_10_R1.PacketPlayOutBlockBreakAnimation packet = new net.minecraft.server.v1_10_R1.PacketPlayOutBlockBreakAnimation(sourceId, position, value);
        net.minecraft.server.v1_10_R1.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer) player).getHandle().playerConnection;
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    public boolean isSlab(Block block) {
        if (block == null || block.getType() == null) {
            return false;
        }
        String name = block.getType().name();
        return name.contains("SLAB") || name.contains("STEP") || name.contains("WOOD_STEP") || name.contains("STONE_SLAB2");
    }

    @SuppressWarnings("deprecation")
    public boolean isUpperHalf(Block block) {
        if (!isSlab(block)) {
            return false;
        }
        if (block.getType().name().contains("DOUBLE")) {
            return true;
        }
        return (block.getData() & 0x8) != 0;
    }
}
