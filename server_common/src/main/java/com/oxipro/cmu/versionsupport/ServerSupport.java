package com.oxipro.cmu.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public interface ServerSupport {

    /**
     * One-minute TPS, or a negative value when it cannot be read.
     */
    double recentTps();

    /**
     * Average milliseconds per tick over the recent samples, or a negative value when it cannot be read.
     */
    double mspt();

    /**
     * Loaded chunks across every world.
     */
    default int loadedChunks() {
        int count = 0;
        for (World world : Bukkit.getWorlds()) {
            Chunk[] chunks = world.getLoadedChunks();
            if (chunks != null) {
                count += chunks.length;
            }
        }
        return count;
    }

    class SupportBuilder {

        private SupportBuilder() {
        }

        /**
         * @return server support for the running version. {@link Server_Default} when no version module matches.
         */
        public static ServerSupport load() {
            ServerSupport support = VersionMapping.load(
                    ServerSupport.class,
                    "com.oxipro.cmu.versionsupport.Server_",
                    "com.oxipro.cmu.versionsupport.Server_Default"
            );
            return support == null ? new Server_Default() : support;
        }
    }
}
