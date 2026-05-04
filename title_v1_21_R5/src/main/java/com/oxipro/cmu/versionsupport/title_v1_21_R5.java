package com.oxipro.cmu.versionsupport;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

//@SuppressWarnings("unused")
//public class title_v1_21_R5 implements TitleSupport {
//
//    @Override
//    public void sendTitle(Player p, String title, String subtitle, int i, int i1, int i2) {
//        if (title != null) {
//            if (!title.isEmpty()) {
//                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
//
//                ClientboundSetTitleTextPacket tit = new ClientboundSetTitleTextPacket(bc);
//                ClientboundSetTitlesAnimationPacket timer = new ClientboundSetTitlesAnimationPacket(i, i1, i2);
//
//                ((CraftPlayer) p).getHandle().c.a(tit);
//                ((CraftPlayer) p).getHandle().c.a(timer);
//            }
//        }
//        if (subtitle != null) {
//            if (!subtitle.isEmpty()) {
//                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
//                ClientboundSetSubtitleTextPacket sub = new ClientboundSetSubtitleTextPacket(bc);
//                ClientboundSetTitlesAnimationPacket length = new ClientboundSetTitlesAnimationPacket(i, i1, i2);
//                ((CraftPlayer) p).getHandle().c.a(sub);
//                ((CraftPlayer) p).getHandle().c.a(length);
//            }
//        }
//    }
//
//    @Override
//    public void playAction(@NotNull Player p, String s) {
//        p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(s));
//    }
//}

