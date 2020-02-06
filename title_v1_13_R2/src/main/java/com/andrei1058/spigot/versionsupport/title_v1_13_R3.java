package com.andrei1058.spigot.versionsupport;

import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class title_v1_13_R3 implements TitleSupport {
    @Override
    public void sendTitle(Player p, String title, String subtitle, int i, int i1, int i2) {
        if (title != null) {
            if (!title.isEmpty()) {
                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
                PacketPlayOutTitle tit = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, bc);
                PacketPlayOutTitle length = new PacketPlayOutTitle(i, i1, i2);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tit);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
            }
        }
        if (subtitle != null) {
            if (!subtitle.isEmpty()) {
                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
                PacketPlayOutTitle tit = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, bc);
                PacketPlayOutTitle length = new PacketPlayOutTitle(i, i1, i2);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tit);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
            }
        }
    }

    @Override
    public void playAction(Player p, String s) {
        CraftPlayer cPlayer = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        cPlayer.getHandle().playerConnection.sendPacket(ppoc);
    }
}
