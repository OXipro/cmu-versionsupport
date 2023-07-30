package com.tomkeuper.spigot.versionsupport;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerUtils_v1_12_R1 implements PlayerUtilsSupport {
    @Override
    public void hidePlayer(Player toBeHidden, Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toBeHidden);
    }

    @Override
    public void unHidePlayer(Player toUnHide, Player receiver, Plugin plugin) {
        receiver.hidePlayer(plugin, toUnHide);
    }
}
