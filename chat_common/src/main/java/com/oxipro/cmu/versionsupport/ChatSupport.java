package com.oxipro.cmu.versionsupport;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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
            String version = VersionMapping.resolveNmsVersion();
            if (version == null) {
                return null;
            }
            switch (version) {
                case "v1_8_R3":
                case "v1_9_R1":
                case "v1_9_R2":
                case "v1_10_R1":
                case "v1_11_R1":
                    return VersionMapping.loadNamed(ChatSupport.class, "com.oxipro.cmu.versionsupport.chat_v1_8_R3");
                default:
                    return VersionMapping.loadNamed(ChatSupport.class, "com.oxipro.cmu.versionsupport.chat_v1_12_R1");
            }
        }
    }
}
