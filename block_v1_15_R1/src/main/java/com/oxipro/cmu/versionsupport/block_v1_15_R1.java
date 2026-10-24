package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;

import java.lang.reflect.Field;

class block_v1_15_R1 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            ageable.setAge(data);
            block.setBlockData(ageable, true);
        }
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        Material material = Material.matchMaterial(new_v1_13);
        if (material == null || !material.isBlock()) {
            Bukkit.getLogger().warning("[CMU] Unknown block for hardness: " + new_v1_13);
            return;
        }
        try {
            net.minecraft.server.v1_15_R1.Block block = CraftMagicNumbers.getBlock(material);
            if (block == null) {
                Bukkit.getLogger().warning("[CMU] Unknown block for hardness: " + new_v1_13);
                return;
            }
            Field field = net.minecraft.server.v1_15_R1.Block.class.getDeclaredField("strength");
            field.setAccessible(true);
            field.setFloat(block, durability);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Bukkit.getLogger().warning("[CMU] Failed to set block hardness for " + new_v1_13 + ": " + e.getMessage());
        }
    }

    public Block getBlockBehindSign(Block sign) {
        if (!(sign.getState().getBlockData() instanceof WallSign)) return null;
        return sign.getRelative(((WallSign) sign.getState().getBlockData()).getFacing().getOppositeFace());
    }

    public float getDurability(org.bukkit.block.Block block) {
        if (block == null) {
            return 0.0F;
        }
        try {
            net.minecraft.server.v1_15_R1.Block type = ((org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock) block).getNMS().getBlock();
            java.lang.reflect.Field field = net.minecraft.server.v1_15_R1.Block.class.getDeclaredField("strength");
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
        net.minecraft.server.v1_15_R1.BlockPosition position = new net.minecraft.server.v1_15_R1.BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreakAnimation packet = new net.minecraft.server.v1_15_R1.PacketPlayOutBlockBreakAnimation(sourceId, position, value);
        net.minecraft.server.v1_15_R1.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer) player).getHandle().playerConnection;
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

}
