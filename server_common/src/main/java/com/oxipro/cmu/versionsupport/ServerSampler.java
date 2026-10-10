package com.oxipro.cmu.versionsupport;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayDeque;

public final class ServerSampler {

    private static final long KEEP_MS = 120_000L;

    private final ArrayDeque<Sample> samples = new ArrayDeque<Sample>();

    public void poll(ServerSupport support) {
        long now = System.currentTimeMillis();
        double mspt = support == null ? -1D : support.mspt();
        double tps = tps(support, mspt);
        double cpu = processCpu();
        samples.addLast(new Sample(now, tps, mspt, cpu));
        long keep = now - KEEP_MS;
        while (!samples.isEmpty() && samples.peekFirst().at < keep) {
            samples.removeFirst();
        }
    }

    public double averageTps(int seconds) {
        return average(seconds, 0);
    }

    public double averageMspt(int seconds) {
        return average(seconds, 1);
    }

    public double averageCpu(int seconds) {
        return average(seconds, 2);
    }

    private double average(int seconds, int kind) {
        int window = seconds <= 0 ? 15 : seconds;
        long cutoff = System.currentTimeMillis() - (window * 1000L);
        double sum = 0D;
        int count = 0;
        for (Sample sample : samples) {
            if (sample.at < cutoff) {
                continue;
            }
            double value = kind == 0 ? sample.tps : kind == 1 ? sample.mspt : sample.cpu;
            if (value < 0D) {
                continue;
            }
            sum += value;
            count++;
        }
        if (count == 0) {
            return -1D;
        }
        return sum / count;
    }

    private static double tps(ServerSupport support, double mspt) {
        if (mspt > 0D) {
            double tps = 1000D / mspt;
            return tps > 20D ? 20D : tps;
        }
        if (support == null) {
            return -1D;
        }
        double recent = support.recentTps();
        return recent < 0D ? -1D : recent;
    }

    private static double processCpu() {
        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        if (!(bean instanceof com.sun.management.OperatingSystemMXBean)) {
            return -1D;
        }
        double load = ((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuLoad();
        if (load < 0D) {
            return -1D;
        }
        if (load > 1D) {
            return 1D;
        }
        return load;
    }

    private static final class Sample {
        private final long at;
        private final double tps;
        private final double mspt;
        private final double cpu;

        private Sample(long at, double tps, double mspt, double cpu) {
            this.at = at;
            this.tps = tps;
            this.mspt = mspt;
            this.cpu = cpu;
        }
    }
}
