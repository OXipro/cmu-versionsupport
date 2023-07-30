package com.tomkeuper.spigot.versionsupport;

import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.plugin.Plugin;

public class cmd_v1_16_R2 implements CommandSupport {

    @Override
    public boolean registerCommand(Plugin plugin, String cmdName, Command instance) {
        return ((CraftServer) plugin.getServer()).getCommandMap().register(cmdName, instance);
    }
}
