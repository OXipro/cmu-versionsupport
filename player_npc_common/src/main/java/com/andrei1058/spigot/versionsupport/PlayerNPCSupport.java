package com.andrei1058.spigot.versionsupport;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public interface PlayerNPCSupport {

    Player spawnNPC(Location location, GameProfile gameProfile);

    Player spawnNPC(Location location, Player player, boolean copyArmor);

    void sendDestroyPacket(Entity entity, Player receiver);

    void sendDestroyPacket(Entity entity, List<Player> receivers);

    void sendDestroyPacket(Entity entity, Collection<? extends Player> receivers);


    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static PlayerNPCSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            try {
                Class<?> c = Class.forName("com.andrei1058.spigot.versionsupport.Player_NPC_" + version);
                return (PlayerNPCSupport) c.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
