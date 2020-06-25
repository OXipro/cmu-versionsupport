package com.andrei1058.spigot.versionsupport;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface ChatSupport {

    void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent textComponent);

    void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent[] textComponent);

    ComponentBuilder append(ComponentBuilder componentBuilder, TextComponent textComponent);

    void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent baseComponent);

    void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent[] baseComponent);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static ChatSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class<?> c;
            try {
                switch (version){
                    case "v1_8_R3":
                    case "v1_9_R1":
                    case "v1_9_R2":
                    case "v1_10_R1":
                    case "v1_11_R1":
                        c = Class.forName("com.andrei1058.spigot.versionsupport.chat_v1_8_R3");
                        break;
                    case "v1_12_R1":
                    case "v1_13_R1":
                    case "v1_13_R2":
                    case "v1_14_R1":
                    case "v1_15_R1":
                    case "v1_16_R1":
                        c = Class.forName("com.andrei1058.spigot.versionsupport.chat_v1_12_R1");
                        break;
                    default:
                        return null;
                }
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (ChatSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
