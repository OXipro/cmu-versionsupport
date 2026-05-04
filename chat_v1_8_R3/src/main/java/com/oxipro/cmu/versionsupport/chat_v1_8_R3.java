package com.oxipro.cmu.versionsupport;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class chat_v1_8_R3 implements ChatSupport {

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent textComponent){
        if (commandSender instanceof Player){
            ((Player) commandSender).spigot().sendMessage(textComponent);
        } else {
            commandSender.sendMessage(textComponent.getText());
        }
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent baseComponent){
        if (commandSender instanceof Player){
            ((Player) commandSender).spigot().sendMessage(baseComponent);
        } else {
            commandSender.sendMessage(baseComponent.toPlainText());
        }
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull TextComponent[] textComponent){
        if (commandSender instanceof Player){
            ((Player) commandSender).spigot().sendMessage(textComponent);
        } else {
            for (TextComponent component : textComponent){
                commandSender.sendMessage(component.getText());
            }
        }
    }

    public void sendMessage(@NotNull CommandSender commandSender, @NotNull BaseComponent[] baseComponent){
        if (commandSender instanceof Player){
            ((Player) commandSender).spigot().sendMessage(baseComponent);
        } else {
            for (BaseComponent component : baseComponent){
                commandSender.sendMessage(component.toPlainText());
            }
        }
    }

    @Override
    public ComponentBuilder append(@NotNull ComponentBuilder componentBuilder, @NotNull TextComponent textComponent) {
        return componentBuilder.append(textComponent.getText()).event(textComponent.getClickEvent()).event(textComponent.getHoverEvent());
    }
}
