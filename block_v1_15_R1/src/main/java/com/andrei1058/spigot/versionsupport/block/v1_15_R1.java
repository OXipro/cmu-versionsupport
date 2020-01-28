package com.andrei1058.spigot.versionsupport.block;

import com.andrei1058.spigot.versionsupport.BlockSupport;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;

import java.lang.reflect.Field;

public class v1_15_R1 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        //todo this requires testing. may not work.
        try {
            Field field = net.minecraft.server.v1_15_R1.Block.class.getDeclaredField("durability");
            field.setAccessible(true);
            field.set(Class.forName("net.minecraft.server.v1_15_R1.Blocks." + new_v1_13.toUpperCase()), durability);

        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Block getBlockBehindSign(Block sign) {
        if (!(sign.getState().getBlockData() instanceof WallSign)) return null;
        return sign.getRelative(((WallSign) sign.getState().getBlockData()).getFacing().getOppositeFace());
    }
}
