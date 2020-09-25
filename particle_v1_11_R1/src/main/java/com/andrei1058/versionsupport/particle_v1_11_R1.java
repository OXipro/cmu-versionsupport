package com.andrei1058.versionsupport;

import com.andrei1058.spigot.versionsupport.ParticleSupport;

public class particle_v1_11_R1 implements ParticleSupport {

    @Override
    public String getForVersion(String v18, String v19, String V12, String V13) {
        return V12;
    }
}
