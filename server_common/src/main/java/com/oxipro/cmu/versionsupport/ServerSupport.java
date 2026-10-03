package com.oxipro.cmu.versionsupport;

public interface ServerSupport {

    /**
     * One-minute TPS reported by the server, or a negative value when this build cannot read it.
     */
    double recentTps();

    final class SupportBuilder {

        private SupportBuilder() {
        }

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
