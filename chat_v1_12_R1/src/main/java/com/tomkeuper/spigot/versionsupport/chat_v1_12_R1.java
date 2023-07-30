package com.tomkeuper.spigot.versionsupport;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class chat_v1_12_R1 implements ChatSupport {

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent textComponent){
        commandSender.spigot().sendMessage(textComponent);
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent baseComponent){
        commandSender.spigot().sendMessage(baseComponent);
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent[] textComponent){
        for (TextComponent component : textComponent){
            sendMessage(commandSender, component);
        }
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent[] baseComponent){
        for (BaseComponent component : baseComponent){
            sendMessage(commandSender, component);
        }
    }

    @Override
    public ComponentBuilder append(@NotNull ComponentBuilder componentBuilder, TextComponent textComponent) {
        return componentBuilder.append(textComponent);
    }
}
