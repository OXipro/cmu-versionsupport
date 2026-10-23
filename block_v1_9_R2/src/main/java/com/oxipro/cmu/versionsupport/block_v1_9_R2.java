package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.lang.reflect.Field;

class block_v1_9_R2 implements BlockSupport {
    public void setBlockData(Block block, byte data) {
        //noinspection deprecation
        block.setData(data, true);
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        try {
            net.minecraft.server.v1_9_R2.Block block = net.minecraft.server.v1_9_R2.Block.getByName(oldName);
            if (block == null) {
                Bukkit.getLogger().warning("[CMU] Unknown block for hardness: " + oldName);
                return;
            }
            Field field = net.minecraft.server.v1_9_R2.Block.class.getDeclaredField("strength");
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
}
