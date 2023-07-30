package com.tomkeuper.spigot.versionsupport;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface ChatSupport {

    /**
     * Send a text component to the command sender.
     *
     * @param commandSender command sender.
     * @param textComponent text component.
     */
    void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent textComponent);

    /**
     * Send a text component list to the command sender.
     *
     * @param commandSender command sender.
     * @param textComponent text component array.
     */
    void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent[] textComponent);

    /**
     * Append content to the component builder.
     *
     * @param componentBuilder component builder.
     * @param textComponent text component.
     */
    ComponentBuilder append(ComponentBuilder componentBuilder, TextComponent textComponent);

    /**
     * Send a baseComponent to the command sender.
     *
     * @param commandSender command sender.
     * @param baseComponent baseComponent.
     */
    void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent baseComponent);

    /**
     * Send a baseComponent array to the command sender.
     *
     * @param commandSender command sender.
     * @param baseComponent baseComponent list.
     */
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
                switch (version) {
                    case "v1_8_R3":
                    case "v1_9_R1":
                    case "v1_9_R2":
                    case "v1_10_R1":
                    case "v1_11_R1":
                        c = Class.forName("com.tomkeuper.spigot.versionsupport.chat_v1_8_R3");
                        break;
                    default:
                        c = Class.forName("com.tomkeuper.spigot.versionsupport.chat_v1_12_R1");

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
