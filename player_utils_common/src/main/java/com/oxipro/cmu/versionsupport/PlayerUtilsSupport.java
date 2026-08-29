package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.oxipro.cmu.versionsupport.VersionMapping.resolveNmsVersion;

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

    default ItemStack getOffHandItem(Player player) {
        try {
            return (ItemStack) PlayerInventory.class
                    .getMethod("getItemInOffHand")
                    .invoke(player.getInventory());
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    class SupportBuilder {

        /**
         * @return support for your server version. Null if not supported.
         */
        @Nullable
        public static PlayerUtilsSupport load() {
            try {
                String version = resolveNmsVersion();
                Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Resolved NMS version: " + version);

                if (version == null) {
                    Bukkit.getLogger().severe("[CMU Debug] PlayerUtils - Unknown server version: " + Bukkit.getBukkitVersion());
                    return null;
                }

                Class<?> c;
                try {
                    String className = "com.oxipro.cmu.versionsupport.PlayerUtils_" + version;
                    Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Trying class: " + className);
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Version-specific class not found, trying major version...");
                    try {
                        String majorVersion = version.substring(0, version.lastIndexOf("_R"));
                        String className = "com.oxipro.cmu.versionsupport.PlayerUtils_" + majorVersion;
                        Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Trying major class: " + className);
                        c = Class.forName(className);
                    } catch (ClassNotFoundException | StringIndexOutOfBoundsException ex) {
                        String className = "com.oxipro.cmu.versionsupport.PlayerUtils_Default";
                        Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Trying fallback class: " + className);
                        c = Class.forName(className);
                    }
                }

                Bukkit.getLogger().info("[CMU Debug] PlayerUtils - Successfully loaded: " + c.getName());
                return (PlayerUtilsSupport) c.getDeclaredConstructor().newInstance();

            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("[CMU Debug] PlayerUtils - No suitable class found (not even Default): " + e.getMessage());
                return null;
            } catch (ReflectiveOperationException e) {
                Bukkit.getLogger().severe("[CMU Debug] PlayerUtils - Failed to instantiate: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
