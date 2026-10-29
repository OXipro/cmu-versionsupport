package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface BlockSupport {

    /**
     * Change a block data only if server version is older than 1.13.
     * On 1.13+ if the given block is Ageable it will assign the given age.
     *
     * @param block target.
     * @param data  block data.
     */
    void setBlockData(Block block, byte data);

    /**
     * Sets the break hardness of a block type for the whole server.
     * This is the destroy speed used to compute how long the block takes to break.
     *
     * @param oldName    vanilla block name for versions before 1.13.
     * @param new_v1_13  material name for 1.13+.
     * @param durability break hardness. 0 breaks instantly.
     */
    void setDurability(String oldName, String new_v1_13, float durability);

    /**
     * @param sign sign block.
     * @return the block behind a sign. Null if given block is not a sign.
     */
    @Nullable
    Block getBlockBehindSign(Block sign);

    /**
     * Break hardness currently stored on the server for this block.
     */
    float getDurability(Block block);

    /**
     * Sends the block-break animation packet on one connection.
     * {@code sourceId} is the animation id from the protocol, not the recipient.
     * Several cracks at once need different ids.
     * Stage 0 to 9 shows the crack. Any other value removes it.
     */
    void sendBlockBreakAnimation(Player player, int sourceId, Block block, int stage);

    /**
     * Reads one block, including its data, without tile-entity contents.
     *
     * @param block target. Null returns null.
     * @return snapshot for {@link #apply(Block, BlockSnapshot)}.
     */
    @Nullable
    BlockSnapshot capture(Block block);

    /**
     * Writes a snapshot back. Neighbor physics stay off.
     *
     * @param block    target.
     * @param snapshot value from {@link #capture(Block)}.
     */
    void apply(Block block, BlockSnapshot snapshot);

    /**
     * Replaces one block with air. Neighbor physics stay off.
     *
     * @param block target.
     */
    void setAir(Block block);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static BlockSupport load() {
            return VersionMapping.load(BlockSupport.class, "com.oxipro.cmu.versionsupport.block_");
        }
    }
}
