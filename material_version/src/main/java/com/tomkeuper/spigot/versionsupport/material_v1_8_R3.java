package com.tomkeuper.spigot.versionsupport;

import org.bukkit.Material;

import javax.annotation.Nullable;

class material_v1_8_R3 implements MaterialSupport {

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
        return material == Material.WOOL;
    }

    public boolean isBed(Material material) {
        return material == Material.BED || material == Material.BED_BLOCK;
    }

    public boolean isGlass(Material material) {
        return material == Material.STAINED_GLASS || material == Material.GLASS;
    }

    public boolean isGlassPane(Material material) {
        return material == Material.STAINED_GLASS_PANE;
    }

    public boolean isTerracotta(Material material) {
        return material == Material.STAINED_CLAY;
    }

    public boolean isConcrete(Material material) {
        return false;
    }

    public boolean isConcretePowder(Material material) {
        return false;
    }

    @Nullable
    public Material getForCurrent(String v1_8, String v1_12, String v1_13) {
        return getMaterial(v1_8);
    }

    public boolean isCake(Material material) {
        return material == Material.CAKE || material == Material.CAKE_BLOCK;
    }

    @Override
    public boolean isSoil(Material material) {
        return material == Material.SOIL;
    }

    @Override
    public Material getSoil() {
        return Material.SOIL;
    }
}
