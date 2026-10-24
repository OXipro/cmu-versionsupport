package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.WallSign;

@SuppressWarnings("unused")
public class block_v1_16_R2 implements BlockSupport {

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
            net.minecraft.server.v1_16_R2.Block nms = org.bukkit.craftbukkit.v1_16_R2.util.CraftMagicNumbers.getBlock(material);
            if (nms == null) {
                org.bukkit.Bukkit.getLogger().warning("[CMU] No NMS block for hardness: " + new_v1_13);
                return;
            }
            java.lang.reflect.Field field = net.minecraft.server.v1_16_R2.BlockBase.BlockData.class.getDeclaredField("strength");
            field.setAccessible(true);
            for (net.minecraft.server.v1_16_R2.IBlockData state : nms.getStates().a()) {
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
            net.minecraft.server.v1_16_R2.IBlockData state = ((org.bukkit.craftbukkit.v1_16_R2.block.CraftBlock) block).getNMS();
            return state.strength;
        } catch (RuntimeException ex) {
            org.bukkit.Bukkit.getLogger().warning("[CMU] Failed to read block hardness: " + ex.getMessage());
            return 0.0F;
        }
    }

    public void sendBlockBreakAnimation(org.bukkit.entity.Player player, int sourceId, org.bukkit.block.Block block, int value) {
        if (player == null || block == null) {
            return;
        }
        net.minecraft.server.v1_16_R2.BlockPosition position = new net.minecraft.server.v1_16_R2.BlockPosition(block.getX(), block.getY(), block.getZ());
        net.minecraft.server.v1_16_R2.PacketPlayOutBlockBreakAnimation packet = new net.minecraft.server.v1_16_R2.PacketPlayOutBlockBreakAnimation(sourceId, position, value);
        net.minecraft.server.v1_16_R2.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer) player).getHandle().playerConnection;
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

}
