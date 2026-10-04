package com.oxipro.cmu.versionsupport;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class World_v1_13_R2 implements WorldSupport {

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
        Chunk chunk = world.getChunkAt(chunkX, chunkZ);
        chunk.setForceLoaded(true);
    }

    @Override
    public void release(World world) {
        if (world == null) {
            return;
        }
        Set<Long> chunks = kept.remove(world.getName());
        if (chunks == null) {
            return;
        }
        for (Long packed : chunks) {
            if (packed == null) {
                continue;
            }
            int chunkX = (int) (packed >> 32);
            int chunkZ = (int) (long) packed;
            Chunk chunk = world.getChunkAt(chunkX, chunkZ);
            chunk.setForceLoaded(false);
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
