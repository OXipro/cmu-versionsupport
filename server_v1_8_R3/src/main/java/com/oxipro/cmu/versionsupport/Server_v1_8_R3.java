package com.oxipro.cmu.versionsupport;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class Server_v1_8_R3 implements ServerSupport {

    @Override
    public double recentTps() {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null || server.recentTps == null || server.recentTps.length == 0) {
            return -1D;
        }
        return server.recentTps[0];
    }
}
