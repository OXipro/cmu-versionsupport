package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.oxipro.cmu.versionsupport.VersionMapping.resolveNmsVersion;

public interface ItemStackSupport {

    /**
     * Get the item in hand.
     *
     * @param player target player.
     * @return the item from given player's main hand.
     */
    @Nullable
    ItemStack getInHand(Player player);

    /**
     * Get the item from off hand. Always null on 1.8.
     *
     * @param player target player.
     * @return the item from given player's off hand.
     */
    @Nullable
    ItemStack getInOffHand(Player player);

    /**
     * Create an item stack.
     *
     * @param material material
     * @param amount   amount.
     * @param data     item data.
     * @return the created itemStack. Null if the given material is invalid.
     */
    @Nullable
    ItemStack createItem(String material, int amount, byte data);

    /**
     * Create an item stack.
     *
     * @param material material
     * @param amount   amount.
     * @param data     item data.
     * @return the created itemStack.
     */
    ItemStack createItem(Material material, int amount, byte data);

    /**
     * Add NBT tag.
     *
     * @param itemStack target item stack.
     * @param key       tag key.
     * @param value     tag value.
     * @return the modified item.
     */
    ItemStack addTag(ItemStack itemStack, String key, String value);

    /**
     * Check if the item has the given NBT tag.
     *
     * @param key       target key.
     * @param itemStack target item stack.
     * @return true if the item have a tag with given key.
     */
    boolean hasTag(ItemStack itemStack, String key);

    /**
     * Get a NBT tag from target item.
     *
     * @param itemStack target item stack.
     * @param key       tag key.
     * @return given key value. Null if tag not exist.
     */
    @Nullable
    String getTag(ItemStack itemStack, String key);

    /**
     * Remove a NBT tag from an item stack.
     *
     * @param key       tag key to be removed.
     * @param itemStack target item.
     * @return modified item stack.
     */
    ItemStack removeTag(ItemStack itemStack, String key);

    /**
     * Make an item unbreakable.
     *
     * @param itemStack   target item.
     * @param unbreakable true or false.
     */
    void setUnbreakable(ItemStack itemStack, boolean unbreakable);

    /**
     * Use you own system to check if the player has enough in his inventory.
     *
     * @param i      target item.
     * @param p      target player.
     * @param amount amount to be removed.
     */
    void minusAmount(Player p, ItemStack i, int amount);

    /**
     * Retrieve how much damage does an item do.
     *
     * @param itemStack target item.
     * @return how much damage gives target item.
     */
    double getDamage(ItemStack itemStack);

    /**
     * Check if the given item stack is an armor part.
     *
     * @param itemStack target item.
     * @return true if it is armor.
     */
    boolean isArmor(ItemStack itemStack);

    /**
     * Check if the given item stack is a tool.
     *
     * @param itemStack target item.
     * @return true if it is a tool.
     */
    boolean isTool(ItemStack itemStack);

    /**
     * Check if the given item stack is a sword.
     *
     * @param itemStack target item.
     * @return true if it is a sword.
     */
    boolean isSword(ItemStack itemStack);

    /**
     * Check if the given item is an axe.
     *
     * @param itemStack target item.
     * @return true if it is an axe.
     */
    boolean isAxe(ItemStack itemStack);

    /**
     * Check if the given item is a bow.
     *
     * @param itemStack target item.
     * @return true if it is a bow.
     */
    boolean isBow(ItemStack itemStack);

    /**
     * Check if the given item is a projectile.
     *
     * @param itemStack target item.
     * @return true if is a projectile.
     */
    boolean isProjectile(ItemStack itemStack);

    /**
     * Check if the given item is a player head.
     *
     * @param itemStack item to be checked.
     * @return true if is a player head.
     */
    boolean isPlayerHead(ItemStack itemStack);

    /**
     * Apply player skin on a head.
     *
     * @param player    player skin owner.
     * @param copyTagFrom item to copy NBTTags from. Nullable.
     * @return player head with player's skin.
     */
    ItemStack applyPlayerSkinOnHead(Player player, ItemStack copyTagFrom);

    /**
     * Apply skin texture on a head.
     *
     * @param texture   texture URI. The part after 'textures.minecraft.net/texture/'.
     *                  You can get textures from minecraft-heads.com.
     * @param itemStack item to copy NBTTags from. Nullable.
     * @return player head with given skin.
     */
    ItemStack applySkinTextureOnHead(String texture, ItemStack itemStack);

    /**
     * Get item data.
     * Will always return 0 for 1.13+.
     */
    default byte getItemData(ItemStack itemStack) {
        return 0;
    }

    /**
     * See if a material is an item that can be given to player
     * Some Materials only exists as block like: end portals and much mroe
     * @param material The material tested
     * @return Bool -> false if material is null
     */
    boolean isItem(Material material);


    void giveRandomItem(Player player, int slot, List<String> blacklist);

    class SupportBuilder {


        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static ItemStackSupport load() {
            try {
                String version = resolveNmsVersion();
                Bukkit.getLogger().info("[CMU Debug] Resolved NMS version: " + version);

                if (version == null) {
                    Bukkit.getLogger().severe("[CMU Debug] Unknown server version: " + Bukkit.getBukkitVersion());
                    return null;
                }

                Class<?> c;
                try {
                    String className = "com.oxipro.cmu.versionsupport.itemstack_" + version;
                    Bukkit.getLogger().info("[CMU Debug] Trying class: " + className);
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    try {
                        String majorVersion = version.substring(0, version.lastIndexOf("_R"));
                        String className = "com.oxipro.cmu.versionsupport.itemstack_" + majorVersion;
                        Bukkit.getLogger().info("[CMU Debug] Trying major class: " + className);
                        c = Class.forName(className);
                    } catch (ClassNotFoundException | StringIndexOutOfBoundsException ex) {
                        Bukkit.getLogger().severe("[CMU Debug] No suitable itemstack class found for: " + version);
                        return null;
                    }
                }

                Bukkit.getLogger().info("[CMU Debug] Successfully loaded: " + c.getName());
                return (ItemStackSupport) c.getDeclaredConstructor().newInstance();

            } catch (ReflectiveOperationException e) {
                Bukkit.getLogger().severe("[CMU Debug] Failed to instantiate: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
