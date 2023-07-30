package com.tomkeuper.spigot.versionsupport;

import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("unused")
public class title_v1_18_R1 implements TitleSupport {

    private static final UUID uuid = new UUID(0L, 0L);

    @Override
    public void sendTitle(Player p, String title, String subtitle, int i, int i1, int i2) {
        if (title != null) {
            if (!title.isEmpty()) {
                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");

                ClientboundSetTitleTextPacket tit = new ClientboundSetTitleTextPacket(bc);
                ClientboundSetTitlesAnimationPacket timer = new ClientboundSetTitlesAnimationPacket(i, i1, i2);

                ((CraftPlayer) p).getHandle().b.a(tit);
                ((CraftPlayer) p).getHandle().b.a(timer);
            }
        }
        if (subtitle != null) {
            if (!subtitle.isEmpty()) {
                IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
                ClientboundSetSubtitleTextPacket sub = new ClientboundSetSubtitleTextPacket(bc);
                ClientboundSetTitlesAnimationPacket length = new ClientboundSetTitlesAnimationPacket(i, i1, i2);
                ((CraftPlayer) p).getHandle().b.a(sub);
                ((CraftPlayer) p).getHandle().b.a(length);
            }
        }
    }

    @Override
    public void playAction(Player p, String s) {
        CraftPlayer cPlayer = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.c, uuid);
        cPlayer.getHandle().b.a(ppoc);
    }
}

