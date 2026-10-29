package com.oxipro.cmu.versionsupport;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class World_v1_8_R3 implements WorldSupport {

    private final Map<String, Set<Long>> kept = new HashMap<String, Set<Long>>();

    @Override
    public void keepChunk(World world, int chunkX, int chunkZ) {
        if (world == null) {
            return;
        }
        Set<Long> chunks = kept.get(world.getName());
        if (chunks == null) {
            chunks = new HashSet<Long>();
            kept.put(world.getName(), chunks);
        }
        if (!chunks.add(pack(chunkX, chunkZ))) {
            return;
        }
        world.loadChunk(chunkX, chunkZ);
    }

    @Override
    public void release(World world) {
        if (world == null) {
            return;
        }
        kept.remove(world.getName());
    }

    @Override
    public boolean setGameRule(World world, String key, String value) {
        if (world == null || key == null || value == null) {
            return false;
        }
        try {
            return world.setGameRuleValue(key, value);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.getChunk() == null || event.getWorld() == null) {
            return;
        }
        Set<Long> chunks = kept.get(event.getWorld().getName());
        if (chunks != null && chunks.contains(pack(event.getChunk().getX(), event.getChunk().getZ()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        release(event.getWorld());
    }

    private static long pack(int chunkX, int chunkZ) {
        return (((long) chunkX) << 32) | (chunkZ & 0xFFFFFFFFL);
    }
}
