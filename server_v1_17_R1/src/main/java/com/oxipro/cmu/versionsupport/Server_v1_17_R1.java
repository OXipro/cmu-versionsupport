package com.oxipro.cmu.versionsupport;

import net.minecraft.server.MinecraftServer;

public class Server_v1_17_R1 implements ServerSupport {

    @Override
    public double recentTps() {
        MinecraftServer server = MinecraftServer.getServer();
        return server == null ? -1D : ServerStats.recentTps(server.recentTps);
    }

    @Override
    public double mspt() {
        MinecraftServer server = MinecraftServer.getServer();
        return server == null ? -1D : ServerStats.mspt(server.n);
    }
}
