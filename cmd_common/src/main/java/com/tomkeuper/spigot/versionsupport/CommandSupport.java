package com.tomkeuper.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public interface CommandSupport {

    /**
     * Register a command.
     *
     * @param plugin   owner.
     * @param cmdName  command name.
     * @param instance command instance.
     */
    boolean registerCommand(Plugin plugin, String cmdName, Command instance);

    class SupportBuilder {

        /**
         * @return cmd support for your server version. Null if not supported.
         */
        @Nullable
        public static CommandSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> c;
            try {
                c = Class.forName("com.tomkeuper.spigot.versionsupport.cmd_" + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (CommandSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
