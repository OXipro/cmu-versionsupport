package com.oxipro.cmu.versionsupport;

import org.bukkit.World;
import org.bukkit.event.Listener;

public interface WorldSupport extends Listener {

    void keepChunk(World world, int chunkX, int chunkZ);

    void release(World world);

    boolean setGameRule(World world, String key, String value);

    class SupportBuilder {

        private SupportBuilder() {
        }

        public static WorldSupport load() {
            return VersionMapping.load(
                    WorldSupport.class,
                    "com.oxipro.cmu.versionsupport.World_"
            );
        }
    }
}
