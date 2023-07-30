package com.tomkeuper.spigot.versionsupport;

import net.minecraft.world.level.block.state.BlockBase;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

import java.lang.reflect.Field;

public class block_v1_19_R3 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        if (block.getBlockData() instanceof Ageable ageable) {
            ageable.setAge(data);
            block.setBlockData(ageable, true);
        }
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        try {
            Field field = BlockBase.class.getDeclaredField("aI");
            field.setAccessible(true);
            field.set(Class.forName("net.minecraft.server.v1_19_R3.Blocks." + new_v1_13.toUpperCase()), durability);

        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Block getBlockBehindSign(Block sign) {
        if (!(sign.getState().getBlockData() instanceof WallSign)) return null;
        return sign.getRelative(((WallSign) sign.getState().getBlockData()).getFacing().getOppositeFace());
    }
}