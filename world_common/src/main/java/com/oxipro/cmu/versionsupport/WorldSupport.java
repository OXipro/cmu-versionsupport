package com.oxipro.cmu.versionsupport;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public interface WorldSupport {

    void enable(Plugin plugin);

    void keepChunk(World world, int chunkX, int chunkZ);

    void release(World world);

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
