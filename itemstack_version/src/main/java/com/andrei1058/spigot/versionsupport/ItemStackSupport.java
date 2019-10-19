package com.andrei1058.spigot.versionsupport;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

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
     */
    void setUnbreakable(ItemStack itemStack);

    /**
     * @param itemStack target item.
     * @param amount    amount to be removed.
     */
    void minusAmount(ItemStack itemStack, int amount);

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
}
