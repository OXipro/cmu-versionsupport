package com.oxipro.cmu.versionsupport;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public interface PlayerUtilsSupport {

    /**
     * An interface for original bukkit Player#hide method
     * since its arguments changed through the versions.
     *
     * @param toBeHidden player to be hidden.
     * @param receiver packet receiver.
     * @param plugin plugin doing the operation.
     */
    void hidePlayer(Player toBeHidden, Player receiver, Plugin plugin);

    /**
     * Show a hidden player. If he was hidden by another plugin I think the new bukkit
     * API will not accept your request.
     * @param toUnHide player to show.
     * @param receiver the player how should see him back.
     * @param plugin plugin doing the request.
     */
    void unHidePlayer(Player toUnHide, Player receiver, Plugin plugin);

    void fakeDamage(Player player);

    void callPlayerDeathEvent(Player player, List<ItemStack> drops, int droppedExp, int newLevel, String deathMessage);

    /**
     * Enable or disable entity collision for the player.
     * Uses Spigot collidesWithEntities on 1.8, Bukkit setCollidable on 1.9+.
     */
    void setCollide(Player player, boolean collide);

    /**
     * Get the item in the player's off hand. Always null on 1.8.
     */
    default ItemStack getOffHandItem(Player player) {
        try {
            return (ItemStack) PlayerInventory.class
                    .getMethod("getItemInOffHand")
                    .invoke(player.getInventory());
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }


    /**
     * Set the player's absorption amount in health points (2 = 1 yellow heart).
     */
    void setAbsorptionHearts(Player player, double amount);

    /**
     * Get the player's absorption amount in health points (2 = 1 yellow heart).
     */
    double getAbsorptionHearts(Player player);

    class SupportBuilder {

        /**
         * @return support for your server version. Null if not supported.
         */
        @Nullable
        public static PlayerUtilsSupport load() {
            return VersionMapping.load(
                    PlayerUtilsSupport.class,
                    "com.oxipro.cmu.versionsupport.PlayerUtils_",
                    "com.oxipro.cmu.versionsupport.PlayerUtils_Default"
            );
        }
    }
}
