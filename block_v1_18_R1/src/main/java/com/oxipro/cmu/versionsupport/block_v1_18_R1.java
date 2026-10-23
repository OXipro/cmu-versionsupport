package com.oxipro.cmu.versionsupport;

import net.minecraft.world.level.block.state.BlockBase;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class block_v1_18_R1 implements BlockSupport {

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
            field.set(Class.forName("net.minecraft.server.v1_18_R1.Blocks." + new_v1_13.toUpperCase()), durability);

        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Block getBlockBehindSign(Block sign) {
        if (!(sign.getState().getBlockData() instanceof WallSign)) return null;
        return sign.getRelative(((WallSign) sign.getState().getBlockData()).getFacing().getOppositeFace());
    }

    public void sendBlockBreakAnimation(org.bukkit.entity.Player player, org.bukkit.block.Block block, int value) {
        if (player == null || block == null) {
            return;
        }
        net.minecraft.core.BlockPosition position = new net.minecraft.core.BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation packet = new net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation(player.getEntityId(), position, value);
        net.minecraft.server.network.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer) player).getHandle().b;
        if (connection != null) {
            connection.a(packet);
        }
    }
}
