package com.andrei1058.spigot.versionsupport;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public interface MaterialSupport {

    /**
     * @param name material name.
     * @return true if material is valid.
     */
    boolean isMaterial(String name);

    /**
     * @param name material name.
     * @return null if material name is invalid.
     */
    @Nullable
    Material getMaterial(String name);

    /**
     * @param name        material name.
     * @param alternative alternative material.
     * @return alternative if name is null.
     */
    Material getMaterialOr(String name, Material alternative);

    /**
     * @param material target material.
     * @return true if given material is wool.
     */
    boolean isWool(Material material);

    /**
     * @param material target material.
     * @return true if given material is bed.
     */
    boolean isBed(Material material);

    /**
     * @param material target material.
     * @return true if given material is glass.
     */
    boolean isGlass(Material material);

    /**
     * @param material target material.
     * @return true if given material is glass pane.
     */
    boolean isGlassPane(Material material);

    /**
     * @param material target material.
     * @return true if given material is stained clay.
     */
    boolean isTerracotta(Material material);

    /**
     * @param material target material.
     * @return true if given material is concrete.
     */
    boolean isConcrete(Material material);

    /**
     * @param material target material.
     * @return true if given material is concrete powder.
     */
    boolean isConcretePowder(Material material);

    /**
     * Get the right material for current version.
     *
     * @param v1_8  material for 1.8 to 1.11 included.
     * @param v1_12 material for 1.12.
     * @param v1_13 material for 1.13 and newer.
     * @return null if material is invalid.
     */
    @Nullable
    Material getForCurrent(String v1_8, String v1_12, String v1_13);

    /**
     * @param material target material.
     * @return true if given material is cake.
     */
    boolean isCake(Material material);

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static MaterialSupport load() {
            String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
            Class c;
            try {
                c = Class.forName("com.andrei1058.spigot.versionsupport.material_" + version);
            } catch (ClassNotFoundException e) {
                //I can't run on your version
                return null;
            }
            try {
                return (MaterialSupport) c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }
    }
}
