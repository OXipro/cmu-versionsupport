package com.andrei1058.spigot.versionsupport;

import net.minecraft.server.v1_16_R3.BlockBase;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

import java.lang.reflect.Field;

public class block_v1_16_R3 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            ageable.setAge(data);
            block.setBlockData(ageable, true);
        }
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        //todo this requires testing. may not work.
        try {
            Field field = BlockBase.class.getDeclaredField("durability");
            field.setAccessible(true);
            field.set(Class.forName("net.minecraft.server.v1_16_R1.Blocks." + new_v1_13.toUpperCase()), durability);

        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Block getBlockBehindSign(Block sign) {
        if (!(sign.getState().getBlockData() instanceof WallSign)) return null;
        return sign.getRelative(((WallSign) sign.getState().getBlockData()).getFacing().getOppositeFace());
    }
}