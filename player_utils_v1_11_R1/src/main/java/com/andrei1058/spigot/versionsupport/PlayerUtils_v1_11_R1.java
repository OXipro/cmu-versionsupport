package com.andrei1058.spigot.versionsupport;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerUtils_v1_11_R1 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, Player receiver, Plugin plugin) {
        receiver.hidePlayer(toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, Player receiver, Plugin plugin) {
        receiver.hidePlayer(toUnHide);
    }
}
