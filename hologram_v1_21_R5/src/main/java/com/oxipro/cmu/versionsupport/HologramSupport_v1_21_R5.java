package com.oxipro.cmu.versionsupport;

import com.oxipro.cmu.versionsupport.hologram.IHolo;
import com.oxipro.cmu.versionsupport.hologram.IHoloLine;
import com.oxipro.cmu.versionsupport.hologram.v1_21_R5.HoloLine;
import com.oxipro.cmu.versionsupport.hologram.v1_21_R5.Hologram;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HologramSupport_v1_21_R5 implements HologramSupport {

    @Override
    public IHolo createHologram(Player player, Location location, String... lines) {
        return createHologram(player, location, toComponents(lines));
    }

    @Override
    public IHolo createHologram(Iterable<Player> players, Location location, String... lines) {
        return createHologram(players, location, toComponents(lines));
    }

    @Override
    public IHolo createHologram(Player player, Location location, Component... lines) {
        List<Component> linesList = new ArrayList<Component>(Arrays.asList(lines));
        Collections.reverse(linesList);
        return new Hologram(player, location, linesList);
    }

    @Override
    public IHolo createHologram(Iterable<Player> players, Location location, Component... lines) {
        List<Component> linesList = new ArrayList<Component>(Arrays.asList(lines));
        Collections.reverse(linesList);
        return new Hologram(players, linesList, location);
    }

    @Override
    public IHolo createHologram(Player player, Location location, IHoloLine... lines) {
        List<IHoloLine> linesList = new ArrayList<IHoloLine>(Arrays.asList(lines));
        Collections.reverse(linesList);
        return new Hologram(player, linesList, location);
    }

    @Override
    public IHolo createHologram(Iterable<Player> players, Location location, IHoloLine... lines) {
        List<IHoloLine> linesList = new ArrayList<IHoloLine>(Arrays.asList(lines));
        Collections.reverse(linesList);
        return new Hologram(players, location, linesList);
    }

    @Override
    public IHoloLine lineFromText(String text, IHolo hologram) {
        return new HoloLine(text, hologram);
    }

    @Override
    public IHoloLine lineFromText(Component text, IHolo hologram) {
        return new HoloLine(text, hologram);
    }

    private static Component[] toComponents(String[] lines) {
        Component[] components = new Component[lines.length];
        for (int i = 0; i < lines.length; i++) {
            components[i] = HologramSupport.fromLegacy(lines[i]);
        }
        return components;
    }
}
