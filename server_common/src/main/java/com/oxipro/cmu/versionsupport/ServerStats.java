package com.oxipro.cmu.versionsupport;

final class ServerStats {

    private ServerStats() {
    }

    static double recentTps(double[] recentTps) {
        if (recentTps == null || recentTps.length == 0) {
            return -1D;
        }
        return recentTps[0];
    }

    static double mspt(long[] tickTimesNanos) {
        if (tickTimesNanos == null || tickTimesNanos.length == 0) {
            return -1D;
        }
        long sum = 0L;
        int samples = 0;
        for (long tickTime : tickTimesNanos) {
            if (tickTime <= 0L) {
                continue;
            }
            sum += tickTime;
            samples++;
        }
        if (samples == 0) {
            return -1D;
        }
        return (sum / (double) samples) / 1000000D;
    }
}
