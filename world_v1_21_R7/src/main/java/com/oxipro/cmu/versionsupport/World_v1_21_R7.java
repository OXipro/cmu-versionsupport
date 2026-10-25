package com.oxipro.cmu.versionsupport;

import org.bukkit.GameRule;
import org.bukkit.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class World_v1_21_R7 extends World_v1_13_R2 {

    private static final Map<String, String> ALIASES = new HashMap<String, String>();
    private static final Map<String, String> INVERTED = new HashMap<String, String>();
    private static final Set<String> REMOVED = new HashSet<String>();

    static {
        alias("mobGriefing", "mob_griefing");
        alias("keepInventory", "keep_inventory");
        alias("doMobSpawning", "spawn_mobs");
        alias("doMobLoot", "mob_drops");
        alias("projectilesCanBreakBlocks", "projectiles_can_break_blocks");
        alias("doTileDrops", "block_drops");
        alias("doEntityDrops", "entity_drops");
        alias("commandBlockOutput", "command_block_output");
        alias("naturalRegeneration", "natural_health_regeneration");
        alias("doDaylightCycle", "advance_time");
        alias("logAdminCommands", "log_admin_commands");
        alias("showDeathMessages", "show_death_messages");
        alias("randomTickSpeed", "random_tick_speed");
        alias("sendCommandFeedback", "send_command_feedback");
        alias("reducedDebugInfo", "reduced_debug_info");
        alias("spectatorsGenerateChunks", "spectators_generate_chunks");
        alias("spawnRadius", "respawn_radius");
        alias("maxEntityCramming", "max_entity_cramming");
        alias("doWeatherCycle", "advance_weather");
        alias("doLimitedCrafting", "limited_crafting");
        alias("maxCommandChainLength", "max_command_sequence_length");
        alias("maxCommandForkCount", "max_command_forks");
        alias("commandModificationBlockLimit", "max_block_modifications");
        alias("announceAdvancements", "show_advancement_messages");
        alias("doInsomnia", "spawn_phantoms");
        alias("doImmediateRespawn", "immediate_respawn");
        alias("playersNetherPortalDefaultDelay", "players_nether_portal_default_delay");
        alias("playersNetherPortalCreativeDelay", "players_nether_portal_creative_delay");
        alias("drowningDamage", "drowning_damage");
        alias("fallDamage", "fall_damage");
        alias("fireDamage", "fire_damage");
        alias("freezeDamage", "freeze_damage");
        alias("doPatrolSpawning", "spawn_patrols");
        alias("doTraderSpawning", "spawn_wandering_traders");
        alias("doWardenSpawning", "spawn_wardens");
        alias("forgiveDeadPlayers", "forgive_dead_players");
        alias("universalAnger", "universal_anger");
        alias("playersSleepingPercentage", "players_sleeping_percentage");
        alias("blockExplosionDropDecay", "block_explosion_drop_decay");
        alias("mobExplosionDropDecay", "mob_explosion_drop_decay");
        alias("tntExplosionDropDecay", "tnt_explosion_drop_decay");
        alias("snowAccumulationHeight", "max_snow_accumulation_height");
        alias("waterSourceConversion", "water_source_conversion");
        alias("lavaSourceConversion", "lava_source_conversion");
        alias("globalSoundEvents", "global_sound_events");
        alias("doVinesSpread", "spread_vines");
        alias("enderPearlsVanishOnDeath", "ender_pearls_vanish_on_death");
        alias("minecartMaxSpeed", "max_minecart_speed");
        alias("tntExplodes", "tnt_explodes");
        alias("locatorBar", "locator_bar");
        alias("pvp", "pvp");
        alias("allowEnteringNetherUsingPortals", "allow_entering_nether_using_portals");
        alias("spawnMonsters", "spawn_monsters");
        alias("commandBlocksEnabled", "command_blocks_work");
        alias("spawnerBlocksEnabled", "spawner_blocks_work");
        INVERTED.put("disablePlayerMovementCheck", "player_movement_check");
        INVERTED.put("disableElytraMovementCheck", "elytra_movement_check");
        INVERTED.put("disableRaids", "raids");
        REMOVED.add("doFireTick");
        REMOVED.add("allowFireTicksAwayFromPlayer");
    }

    private static void alias(String oldName, String newName) {
        ALIASES.put(oldName, newName);
    }

    @Override
    public boolean setGameRule(World world, String key, String value) {
        if (world == null || key == null || value == null) {
            return false;
        }
        String name = strip(key);
        if (name.isEmpty() || REMOVED.contains(name)) {
            return false;
        }
        String inverted = INVERTED.get(name);
        if (inverted != null) {
            String flipped = flip(value);
            if (flipped == null) {
                return false;
            }
            if (applyRule(world, inverted, flipped)) {
                return true;
            }
        }
        String alias = ALIASES.get(name);
        if (alias != null && applyRule(world, alias, value)) {
            return true;
        }
        if (alias != null && alias.equals(name)) {
            return false;
        }
        return applyRule(world, name, value);
    }

    private static String strip(String key) {
        if (key.regionMatches(true, 0, "minecraft:", 0, 10)) {
            return key.substring(10);
        }
        return key;
    }

    private static String flip(String value) {
        String trimmed = value.trim();
        if (trimmed.equalsIgnoreCase("true")) {
            return "false";
        }
        if (trimmed.equalsIgnoreCase("false")) {
            return "true";
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static boolean applyRule(World world, String name, String value) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        GameRule<?> rule;
        try {
            rule = GameRule.getByName(name);
        } catch (RuntimeException ex) {
            return false;
        }
        if (rule == null) {
            return false;
        }
        try {
            if (Boolean.class.equals(rule.getType())) {
                Boolean parsed = parseBoolean(value);
                if (parsed == null) {
                    return false;
                }
                return world.setGameRule((GameRule<Boolean>) rule, parsed);
            }
            if (Integer.class.equals(rule.getType())) {
                Integer parsed = parseInt(value);
                if (parsed == null) {
                    return false;
                }
                return world.setGameRule((GameRule<Integer>) rule, parsed);
            }
        } catch (RuntimeException ex) {
            return false;
        }
        return false;
    }

    private static Boolean parseBoolean(String value) {
        String trimmed = value.trim();
        if (trimmed.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (trimmed.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        return null;
    }

    private static Integer parseInt(String value) {
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
