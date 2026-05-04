package com.oxipro.cmu.versionsupport;

import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.plugin.Plugin;

public class cmd_v1_17_R1  implements CommandSupport {

    @Override
    public boolean registerCommand(Plugin plugin, String cmdName, Command instance) {
        return ((CraftServer) plugin.getServer()).getCommandMap().register(cmdName, instance);
    }
}
