package com.andrei1058.spigot.versionsupport.material;

import org.bukkit.Material;

import javax.annotation.Nullable;

public class v1_12_R1 extends v1_8_R3 {

    @Override
    public boolean isConcrete(Material material) {
        return material.toString().equals("CONCRETE");
    }

    @Override
    public boolean isConcretePowder(Material material) {
        return material.toString().equals("CONCRETE_POWDER");
    }

    @Nullable
    @Override
    public Material getForCurrent(String v1_8, String v1_12, String v1_13) {
        return getMaterial(v1_12);
    }
}
