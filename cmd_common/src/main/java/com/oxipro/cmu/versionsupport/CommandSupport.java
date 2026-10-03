package com.oxipro.cmu.versionsupport;

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
            return VersionMapping.load(CommandSupport.class, "com.oxipro.cmu.versionsupport.cmd_");
        }
    }
}
