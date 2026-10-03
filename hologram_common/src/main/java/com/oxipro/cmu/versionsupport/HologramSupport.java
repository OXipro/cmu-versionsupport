package com.oxipro.cmu.versionsupport;

import com.oxipro.cmu.versionsupport.hologram.IHolo;
import com.oxipro.cmu.versionsupport.hologram.IHoloLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface HologramSupport {

    IHolo createHologram(Player player, Location location, String... lines);

    IHolo createHologram(Iterable<Player> players, Location location, String... lines);

    IHolo createHologram(Player player, Location location, Component... lines);

    IHolo createHologram(Iterable<Player> players, Location location, Component... lines);

    IHolo createHologram(Player player, Location location, IHoloLine... lines);

    IHolo createHologram(Iterable<Player> players, Location location, IHoloLine... lines);

    IHoloLine lineFromText(String text, IHolo hologram);

    IHoloLine lineFromText(Component text, IHolo hologram);

    default IHolo createHologram(Player player, Entity entity, String... lines) {
        IHolo hologram = createHologram(player, entity.getLocation(), lines);
        hologram.attach(entity);
        return hologram;
    }

    default IHolo createHologram(Iterable<Player> players, Entity entity, String... lines) {
        IHolo hologram = createHologram(players, entity.getLocation(), lines);
        hologram.attach(entity);
        return hologram;
    }

    default IHolo createHologram(Player player, Entity entity, Component... lines) {
        IHolo hologram = createHologram(player, entity.getLocation(), lines);
        hologram.attach(entity);
        return hologram;
    }

    default IHolo createHologram(Iterable<Player> players, Entity entity, Component... lines) {
        IHolo hologram = createHologram(players, entity.getLocation(), lines);
        hologram.attach(entity);
        return hologram;
    }

    default IHolo createHologram(Player player, Entity entity, double yOffset, String... lines) {
        IHolo hologram = createHologram(player, entity.getLocation(), lines);
        hologram.attach(entity, yOffset);
        return hologram;
    }

    default IHolo createHologram(Iterable<Player> players, Entity entity, double yOffset, String... lines) {
        IHolo hologram = createHologram(players, entity.getLocation(), lines);
        hologram.attach(entity, yOffset);
        return hologram;
    }

    default IHolo createHologram(Player player, Entity entity, double yOffset, Component... lines) {
        IHolo hologram = createHologram(player, entity.getLocation(), lines);
        hologram.attach(entity, yOffset);
        return hologram;
    }

    default IHolo createHologram(Iterable<Player> players, Entity entity, double yOffset, Component... lines) {
        IHolo hologram = createHologram(players, entity.getLocation(), lines);
        hologram.attach(entity, yOffset);
        return hologram;
    }

    LegacyComponentSerializer LEGACY_SECTION = LegacyComponentSerializer.builder()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    static Component fromLegacy(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        return LEGACY_SECTION.deserialize(text.replace('&', LegacyComponentSerializer.SECTION_CHAR));
    }

    static String toLegacy(Component component) {
        if (component == null || Component.empty().equals(component)) {
            return "";
        }
        return LEGACY_SECTION.serialize(component);
    }

    class SupportBuilder {

        /**
         * @return hologram support for the running server version, or null if unsupported.
         */
        @Nullable
        public static HologramSupport load() {
            return VersionMapping.load(HologramSupport.class, "com.oxipro.cmu.versionsupport.HologramSupport_");
        }
    }
}
