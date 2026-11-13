package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.Location;
import org.bukkit.block.data.type.Switch;

public class block_v1_20_R2 implements BlockSupport {

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
            net.minecraft.world.level.block.Block nms = org.bukkit.craftbukkit.v1_20_R2.util.CraftMagicNumbers.getBlock(material);
            if (nms == null) {
                org.bukkit.Bukkit.getLogger().warning("[CMU] No NMS block for hardness: " + new_v1_13);
                return;
            }
            java.lang.reflect.Field field = net.minecraft.world.level.block.state.BlockBase.BlockData.class.getDeclaredField("n");
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
            net.minecraft.world.level.block.state.IBlockData state = ((org.bukkit.craftbukkit.v1_20_R2.block.CraftBlock) block).getNMS();
            return state.n;
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
        net.minecraft.server.network.PlayerConnection connection = ((org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer) player).getHandle().c;
        if (connection != null) {
            connection.b(packet);
        }
    }


    public BlockSnapshot capture(Block block) {
        if (block == null) {
            return null;
        }
        return new BlockSnapshot(0, (byte) 0, block.getBlockData());
    }

    public void apply(Block block, BlockSnapshot snapshot) {
        if (block == null || snapshot == null || !(snapshot.modernState instanceof org.bukkit.block.data.BlockData)) {
            return;
        }
        block.setBlockData((org.bukkit.block.data.BlockData) snapshot.modernState, false);
    }

    public void setAir(Block block) {
        if (block == null) {
            return;
        }
        block.setType(org.bukkit.Material.AIR, false);
    }

    public boolean isSlab(Block block) {
        return block != null && block.getBlockData() instanceof Slab;
    }

    public boolean isUpperHalf(Block block) {
        if (block == null || !(block.getBlockData() instanceof Slab)) {
            return false;
        }
        Slab.Type type = ((Slab) block.getBlockData()).getType();
        return type == Slab.Type.TOP || type == Slab.Type.DOUBLE;
    }

    public Location mechanismCenter(Block block) {
        if (block == null || block.getType() == null) {
            return null;
        }
        if (block.getBlockData() instanceof Switch) {
            Switch data = (Switch) block.getBlockData();
            if (data.getFace() == Switch.Face.FLOOR) {
                return at(block, 0.5D, 1D / 16D, 0.5D);
            }
            if (data.getFace() == Switch.Face.CEILING) {
                return at(block, 0.5D, 15D / 16D, 0.5D);
            }
            switch (data.getFacing()) {
                case SOUTH:
                    return at(block, 0.5D, 0.5D, 1D / 16D);
                case EAST:
                    return at(block, 1D / 16D, 0.5D, 0.5D);
                case WEST:
                    return at(block, 15D / 16D, 0.5D, 0.5D);
                case NORTH:
                    return at(block, 0.5D, 0.5D, 15D / 16D);
                default:
                    return at(block, 0.5D, 0.5D, 0.5D);
            }
        }
        if (block.getType().name().endsWith("_PRESSURE_PLATE")) {
            return at(block, 0.5D, 1D / 32D, 0.5D);
        }
        return null;
    }

    private Location at(Block block, double x, double y, double z) {
        return new Location(block.getWorld(), block.getX() + x, block.getY() + y, block.getZ() + z);
    }
}
