package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

public class block_v1_21_R6 implements BlockSupport {

    public void setBlockData(Block block, byte data) {
        if (block.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) block.getBlockData();
            ageable.setAge(data);
            block.setBlockData(ageable, true);
        }
    }

    public void setDurability(String oldName, String new_v1_13, float durability) {
        org.bukkit.Material material = org.bukkit.Material.matchMaterial(new_v1_13);
        if (material == null || !material.isBlock()) {
            org.bukkit.Bukkit.getLogger().warning("[CMU] Unknown block for hardness: " + new_v1_13);
            return;
        }
        try {
            net.minecraft.world.level.block.Block nms = org.bukkit.craftbukkit.v1_21_R6.util.CraftMagicNumbers.getBlock(material);
            if (nms == null) {
                org.bukkit.Bukkit.getLogger().warning("[CMU] No NMS block for hardness: " + new_v1_13);
                return;
            }
            java.lang.reflect.Field field = net.minecraft.world.level.block.state.BlockBase.BlockData.class.getDeclaredField("p");
            field.setAccessible(true);
            for (net.minecraft.world.level.block.state.IBlockData state : nms.l().a()) {
                field.setFloat(state, durability);
            }
        } catch (ReflectiveOperationException e) {
            org.bukkit.Bukkit.getLogger().warning("[CMU] Failed to set block hardness for " + new_v1_13 + ": " + e.getMessage());
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
            net.minecraft.world.level.block.state.IBlockData state = ((org.bukkit.craftbukkit.v1_21_R6.block.CraftBlock) block).getNMS();
            return state.p;
        } catch (RuntimeException ex) {
            org.bukkit.Bukkit.getLogger().warning("[CMU] Failed to read block hardness: " + ex.getMessage());
            return 0.0F;
        }
    }

    public void sendBlockBreakAnimation(org.bukkit.entity.Player player, int sourceId, org.bukkit.block.Block block, int value) {
        if (player == null || block == null) {
            return;
        }
        net.minecraft.core.BlockPosition position = new net.minecraft.core.BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation packet = new net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation(sourceId, position, value);
        net.minecraft.server.network.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer) player).getHandle().g;
        if (connection != null) {
            connection.b(packet);
        }
    }

}
