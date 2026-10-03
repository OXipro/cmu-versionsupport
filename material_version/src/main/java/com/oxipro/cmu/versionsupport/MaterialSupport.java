package com.oxipro.cmu.versionsupport;

import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.Locale;

public interface MaterialSupport {

    /**
     * Wool / terracotta color names in legacy data order (0-15).
     */
    String[] COLOR_NAMES = {
            "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE",
            "YELLOW", "LIME", "PINK", "GRAY",
            "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE",
            "BROWN", "GREEN", "RED", "BLACK"
    };

    /**
     * Check if the given string is a valid material for the current server version.
     *
     * @param name material name.
     * @return true if material is valid.
     */
    boolean isMaterial(String name);

    /**
     * Get material by name.
     *
     * @param name material name.
     * @return null if material name is invalid.
     */
    @Nullable
    Material getMaterial(String name);

    /**
     * Get material by name or fallback material.
     *
     * @param name        material name.
     * @param alternative alternative material.
     * @return alternative if name is null.
     */
    Material getMaterialOr(String name, Material alternative);

    /**
     * Check if the given material is wool.
     *
     * @param material target material.
     * @return true if given material is wool.
     */
    boolean isWool(Material material);

    /**
     * Check if the given material is a bed.
     *
     * @param material target material.
     * @return true if given material is bed.
     */
    boolean isBed(Material material);

    /**
     * Check if the given material is glass.
     *
     * @param material target material.
     * @return true if given material is glass.
     */
    boolean isGlass(Material material);

    /**
     * Check if the given material is a glass pane.
     *
     * @param material target material.
     * @return true if given material is glass pane.
     */
    boolean isGlassPane(Material material);

    /**
     * Check if the given material is terracotta.
     *
     * @param material target material.
     * @return true if given material is stained clay.
     */
    boolean isTerracotta(Material material);

    /**
     * Check if the given material is concrete.
     *
     * @param material target material.
     * @return true if given material is concrete.
     */
    boolean isConcrete(Material material);

    /**
     * Check if the given material is concrete powder.
     *
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
     * Check if the given material is cake.
     *
     * @param material target material.
     * @return true if given material is cake.
     */
    boolean isCake(Material material);

    /**
     * Check if the given material is soil.
     */
    boolean isSoil(Material material);

    Material getSoil();

    /**
     * True on 1.8-1.12, where wool / stained clay color is stored as item data.
     * 1.13+ uses distinct materials ({@code RED_WOOL}, {@code BLUE_TERRACOTTA}, ...).
     */
    default boolean usesLegacyColorData() {
        return getMaterial("WHITE_WOOL") == null;
    }

    /**
     * Color name for a wool data value (0-15), e.g. {@code RED}.
     */
    default String colorName(int data) {
        int index = data;
        if (index < 0 || index >= COLOR_NAMES.length) {
            index = 0;
        }
        return COLOR_NAMES[index];
    }

    /**
     * Wool data (0-15) for a dye / material color name.
     * Accepts {@code RED}, {@code SILVER}, {@code LIGHT_GRAY}, {@code RED_WOOL}, ...
     */
    default int colorData(String color) {
        String key = normalizeColor(color);
        if (key == null) {
            return 0;
        }
        try {
            int parsed = Integer.parseInt(key);
            if (parsed >= 0 && parsed < COLOR_NAMES.length) {
                return parsed;
            }
        } catch (NumberFormatException ignored) {
        }
        for (int i = 0; i < COLOR_NAMES.length; i++) {
            if (COLOR_NAMES[i].equals(key)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * White wool on 1.13+, {@code WOOL} on 1.8-1.12.
     */
    @Nullable
    default Material getWool() {
        return getWool(0);
    }

    /**
     * Wool for the given wool data (0-15).
     * 1.8-1.12 always returns {@code WOOL}; apply {@code data} on the item stack.
     * 1.13+ returns {@code WHITE_WOOL}, {@code RED_WOOL}, ...
     */
    @Nullable
    default Material getWool(int data) {
        if (usesLegacyColorData()) {
            return getMaterial("WOOL");
        }
        Material colored = getMaterial(colorName(data) + "_WOOL");
        return colored != null ? colored : getMaterial("WHITE_WOOL");
    }

    /**
     * Wool for a dye / material color name.
     */
    @Nullable
    default Material getWool(String color) {
        return getWool(colorData(color));
    }

    /**
     * White terracotta / stained clay.
     */
    @Nullable
    default Material getTerracotta() {
        return getTerracotta(0);
    }

    /**
     * Terracotta for the given wool data (0-15).
     * 1.8-1.12 returns {@code STAINED_CLAY}; apply {@code data} on the item stack.
     * 1.13+ returns {@code WHITE_TERRACOTTA}, {@code RED_TERRACOTTA}, ...
     */
    @Nullable
    default Material getTerracotta(int data) {
        if (usesLegacyColorData()) {
            Material clay = getMaterial("STAINED_CLAY");
            return clay != null ? clay : getMaterial("HARD_CLAY");
        }
        Material colored = getMaterial(colorName(data) + "_TERRACOTTA");
        return colored != null ? colored : getMaterial("WHITE_TERRACOTTA");
    }

    /**
     * Terracotta for a dye / material color name.
     */
    @Nullable
    default Material getTerracotta(String color) {
        return getTerracotta(colorData(color));
    }

    @Nullable
    default Material getGlassPane() {
        return getGlassPane(0);
    }

    @Nullable
    default Material getGlassPane(int data) {
        if (usesLegacyColorData()) {
            return getMaterial("STAINED_GLASS_PANE");
        }
        Material colored = getMaterial(colorName(data) + "_STAINED_GLASS_PANE");
        return colored != null ? colored : getMaterial("WHITE_STAINED_GLASS_PANE");
    }

    @Nullable
    default Material getGlassPane(String color) {
        return getGlassPane(colorData(color));
    }

    @Nullable
    default Material resolve(String name) {
        return resolve(name, 0);
    }

    @Nullable
    default Material resolve(String name, int data) {
        if (name == null) {
            return getMaterial("STONE");
        }
        String key = name.trim().toUpperCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
        if (key.isEmpty()) {
            return getMaterial("STONE");
        }
        Material direct = getMaterial(key);
        if (direct != null) {
            return direct;
        }
        int color = colorData(key);
        if (color == 0) {
            color = data;
        }
        if ("WOOL".equals(key) || "LEGACY_WOOL".equals(key) || key.endsWith("_WOOL")) {
            return getWool(color);
        }
        if ("TERRACOTTA".equals(key) || "STAINED_CLAY".equals(key) || "HARD_CLAY".equals(key)
                || "LEGACY_STAINED_CLAY".equals(key) || key.endsWith("_TERRACOTTA") || key.endsWith("_STAINED_CLAY")) {
            return getTerracotta(color);
        }
        if ("STAINED_GLASS_PANE".equals(key) || "LEGACY_STAINED_GLASS_PANE".equals(key) || key.endsWith("_STAINED_GLASS_PANE")) {
            return getGlassPane(color);
        }
        return getMaterial("STONE");
    }

    default byte resolveData(String name, byte data) {
        if (!usesLegacyColorData()) {
            return 0;
        }
        if (name == null) {
            return data;
        }
        String key = name.trim().toUpperCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
        if (getMaterial(key) != null) {
            return data;
        }
        int color = colorData(key);
        return (byte) (color == 0 ? data : color);
    }

    default String normalizeColor(String color) {
        if (color == null) {
            return null;
        }
        String key = color.trim().toUpperCase(Locale.ROOT).replace(' ', '_').replace('-', '_');
        if (key.isEmpty()) {
            return null;
        }
        key = stripColorSuffix(key);
        if ("GREY".equals(key)) {
            return "GRAY";
        }
        if ("SILVER".equals(key) || "LIGHTGRAY".equals(key) || "LIGHT_GREY".equals(key) || "GREY_LIGHT".equals(key)) {
            return "LIGHT_GRAY";
        }
        return key;
    }

    default String stripColorSuffix(String key) {
        String[] suffixes = {
                "_WOOL", "_TERRACOTTA", "_STAINED_CLAY", "_STAINED_GLASS_PANE",
                "_STAINED_GLASS", "_CONCRETE_POWDER", "_CONCRETE", "_GLASS_PANE", "_GLASS"
        };
        for (String suffix : suffixes) {
            if (key.endsWith(suffix) && key.length() > suffix.length()) {
                return key.substring(0, key.length() - suffix.length());
            }
        }
        return key;
    }

    class SupportBuilder {

        /**
         * @return block support for your server version. Null if not supported.
         */
        @Nullable
        public static MaterialSupport load() {
            return VersionMapping.load(
                    MaterialSupport.class,
                    "com.oxipro.cmu.versionsupport.material_",
                    "com.oxipro.cmu.versionsupport.material_v1_13_R2"
            );
        }
    }
}
