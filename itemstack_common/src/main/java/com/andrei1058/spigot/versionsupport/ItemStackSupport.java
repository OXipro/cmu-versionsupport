package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public interface ItemStackSupport {

    /**
     * @param player target player.
     * @return the item from given player's main hand.
     */
    @Nullable
    ItemStack getInHand(Player player);

    /**
     * @param player target player.
     * @return the item from given player's off hand.
     */
    @Nullable
    ItemStack getInOffHand(Player player);

    /**
     * @param material material
     * @param amount   amount.
     * @param data     item data.
     * @return the created itemStack. Null if the given material is invalid.
     */
    @Nullable
    ItemStack createItem(String material, int amount, byte data);

    /**
     * @param material material
     * @param amount   amount.
     * @param data     item data.
     * @return the created itemStack.
     */
    ItemStack createItem(Material material, int amount, byte data);

    /**
     * @param itemStack target item stack.
     * @param key       tag key.
     * @param value     tag value.
     * @return the modified item.
     */
    ItemStack addTag(ItemStack itemStack, String key, String value);

    /**
     * @param key       target key.
     * @param itemStack target item stack.
     * @return true if the item have a tag with given key.
     */
    boolean hasTag(ItemStack itemStack, String key);

    /**
     * @param itemStack target item stack.
     * @param key       tag key.
     * @return given key value. Null if tag not exist.
     */
    @Nullable
    String getTag(ItemStack itemStack, String key);

    /**
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
     * @param itemStack target item.
     * @return how much damage gives target item.
     */
    double getDamage(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @return true if it is armor.
     */
    boolean isArmor(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @return true if it is a tool.
     */
    boolean isTool(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @return true if it is a sword.
     */
    boolean isSword(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @return true if it is an axe.
     */
    boolean isAxe(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @return true if it is a bow.
     */
    boolean isBow(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @return true if is a projectile.
     */
    boolean isProjectile(ItemStack itemStack);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static ItemStackSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class c;
            try {
                c = Class.forName("com.andrei1058.spigot.versionsupport.itemstack." + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (ItemStackSupport) c.getConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
    }
}
