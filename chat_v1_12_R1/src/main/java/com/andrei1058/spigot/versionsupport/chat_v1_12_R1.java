package com.andrei1058.spigot.versionsupport;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class chat_v1_12_R1 implements ChatSupport {

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent textComponent){
        commandSender.spigot().sendMessage(textComponent);
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent[] textComponent){
        for (TextComponent component : textComponent){
            sendMessage(commandSender, component);
        }
    }

    @Override
    public ComponentBuilder append(@NotNull ComponentBuilder componentBuilder, TextComponent textComponent) {
        return componentBuilder.append(textComponent);
    }
}
