package com.andrei1058.spigot.versionsupport.material;

import com.andrei1058.spigot.versionsupport.MaterialSupport;
import org.bukkit.Material;

import javax.annotation.Nullable;

public class v1_13_R1 implements MaterialSupport {

    public boolean isMaterial(String name) {
        try {
            Material.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Nullable
    public Material getMaterial(String name) {
        Material m = null;
        try {
            m = Material.valueOf(name.toUpperCase());
        } catch (Exception ignored) {
        }
        return m;
    }

    public Material getMaterialOr(String name, Material alternative) {
        Material m = getMaterial(name);
        return m == null ? alternative : m;
    }

    public boolean isWool(Material material) {
        return material.toString().endsWith("_WOOL");
    }

    public boolean isBed(Material material) {
        return material.toString().endsWith("_BED");
    }

    public boolean isGlass(Material material) {
        return material.toString().endsWith("_STAINED_GLASS") || material.toString().equals("GLASS");
    }

    public boolean isGlassPane(Material material) {
        return material.toString().endsWith("_STAINED_GLASS_PANE");
    }

    public boolean isTerracotta(Material material) {
        return material.toString().endsWith("_TERRACOTTA");
    }

    public boolean isConcrete(Material material) {
        return material.toString().endsWith("_CONCRETE");
    }

    public boolean isConcretePowder(Material material) {
        return material.toString().endsWith("_CONCRETE_POWDER");
    }

    @Nullable
    public Material getForCurrent(String v1_8, String v1_12, String v1_13) {
        return getMaterial(v1_13);
    }

    public boolean isCake(Material material) {
        return material.toString().equals("CAKE");
    }
}
