    //package com.oxipro.cmu.versionsupport;
    //
    //import net.minecraft.network.chat.IChatBaseComponent;
    //import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
    //import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
    //import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
    //import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
    //import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
    //import org.bukkit.entity.Player;
    //import org.jetbrains.annotations.NotNull;
    //
    //@SuppressWarnings("unused")
    //public class title_v1_21_R5 implements TitleSupport {
    //
    //    @Override
    //    public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    //        // ⚠️ Vérifie le nom du champ connection sur EntityPlayer dans ton jar
    //        // En 1.20 R3 c'était .c — en 1.21 R5 c'est souvent .b ou .c selon le build
    //        // Décompile net.minecraft.server.level.EntityPlayer et cherche le champ PlayerConnection
    //        var connection = ((CraftPlayer) p).getHandle().c; // <- À VÉRIFIER
    //
    //        if (title != null && !title.isEmpty()) {
    //            IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
    //            ClientboundSetTitlesAnimationPacket timer = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
    //            ClientboundSetTitleTextPacket tit = new ClientboundSetTitleTextPacket(bc);
    //            connection.b(timer);
    //            connection.b(tit);
    //        }
    //
    //        if (subtitle != null && !subtitle.isEmpty()) {
    //            IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
    //            ClientboundSetTitlesAnimationPacket timer = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
    //            ClientboundSetSubtitleTextPacket sub = new ClientboundSetSubtitleTextPacket(bc);
    //            connection.b(timer);
    //            connection.b(sub);
    //        }
    //    }
    //
    //    @Override
    //    public void playAction(@NotNull Player p, String text) {
    //        IChatBaseComponent bc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
    //        ClientboundSetActionBarTextPacket packet = new ClientboundSetActionBarTextPacket(bc);
    //        ((CraftPlayer) p).getHandle().c.b(packet); // <- même champ/méthode à vérifier
    //    }
    //}