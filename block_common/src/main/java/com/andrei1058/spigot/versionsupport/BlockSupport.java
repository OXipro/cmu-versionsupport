package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import javax.annotation.Nullable;

public interface BlockSupport {

    /**
     * Change a block data only if server version is older than 1.13.
     *
     * @param block target.
     * @param data  block data.
     */
    void setBlockData(Block block, byte data);

    /**
     * Change a block durability.
     *
     * @param oldName    vanilla block name for versions before 1.13.
     * @param new_v1_13  enum name for 1.13+.
     * @param durability durability.
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
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> c;
            try {
                c = Class.forName("com.andrei1058.spigot.versionsupport.block_" + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (BlockSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
