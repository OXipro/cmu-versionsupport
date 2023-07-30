package com.tomkeuper.spigot.versionsupport;

public class particle_v1_12_R1 implements ParticleSupport {
    @Override
    public String getForVersion(String v18, String v19, String V12, String V13) {
        return V12;
    }
}