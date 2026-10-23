package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

public class block_v1_21_R4 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            ageable.setAge(data);
            block.setBlockData(ageable, true);
        }
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
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
        net.minecraft.server.network.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer) player).getHandle().g;
        if (connection != null) {
            connection.b(packet);
        }
    }

}
