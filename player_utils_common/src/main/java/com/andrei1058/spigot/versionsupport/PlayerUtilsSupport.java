package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

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

    class SupportBuilder {

        /**
         * @return support for your server version. Null if not supported.
         */
        @Nullable
        public static PlayerUtilsSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            try {
                Class<?> c = Class.forName("com.andrei1058.spigot.versionsupport.PlayerUtils_" + version);
                return (PlayerUtilsSupport) c.getConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
