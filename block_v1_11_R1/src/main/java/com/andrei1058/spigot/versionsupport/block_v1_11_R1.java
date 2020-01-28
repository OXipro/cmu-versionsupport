package com.andrei1058.spigot.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.lang.reflect.Field;

class block_v1_11_R1 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        //noinspection deprecation
        block.setData(data, true);
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        try {
            Field field = net.minecraft.server.v1_11_R1.Block.class.getDeclaredField("durability");
            field.setAccessible(true);
            field.set(net.minecraft.server.v1_11_R1.Block.getByName(oldName), durability);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Block getBlockBehindSign(Block sign) {
        BlockState block = sign.getState();
        if (!(block instanceof org.bukkit.material.Sign)) return null;
        return block.getBlock().getRelative(((org.bukkit.material.Sign) block.getData()).getAttachedFace());
    }
}
