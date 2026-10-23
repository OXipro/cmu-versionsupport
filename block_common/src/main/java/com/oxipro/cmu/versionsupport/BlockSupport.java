package com.oxipro.cmu.versionsupport;

import org.bukkit.block.Block;

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
