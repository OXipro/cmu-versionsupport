package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

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
}
