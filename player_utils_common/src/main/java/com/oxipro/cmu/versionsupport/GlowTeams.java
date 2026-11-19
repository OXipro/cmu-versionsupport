package com.oxipro.cmu.versionsupport;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class GlowTeams {

    private static final Map<UUID, Map<Integer, String>> ACTIVE = new HashMap<UUID, Map<Integer, String>>();

    private GlowTeams() {
    }

    static String entry(Entity entity) {
        if (entity instanceof Player) {
            return ((Player) entity).getName();
        }
        return entity.getUniqueId().toString();
    }

    static String name(int entityId, GlowColor color) {
        return "g" + Integer.toHexString(color.id() & 0xF) + Integer.toString(entityId, 36);
    }

    static String take(UUID viewer, int entityId) {
        Map<Integer, String> teams = ACTIVE.get(viewer);
        if (teams == null) {
            return null;
        }
        String name = teams.remove(entityId);
        if (teams.isEmpty()) {
            ACTIVE.remove(viewer);
        }
        return name;
    }

    static void keep(UUID viewer, int entityId, String name) {
        Map<Integer, String> teams = ACTIVE.get(viewer);
        if (teams == null) {
            teams = new HashMap<Integer, String>();
            ACTIVE.put(viewer, teams);
        }
        teams.put(entityId, name);
    }
}
