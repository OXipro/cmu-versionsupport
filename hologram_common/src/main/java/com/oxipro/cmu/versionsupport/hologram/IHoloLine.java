package com.oxipro.cmu.versionsupport.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface IHoloLine {

    /**
     * Legacy section-serialized text of this line.
     */
    String getText();

    Component getComponent();

    IHolo getHologram();

    void update();

    void update(Player player);

    boolean isDestroyed();

    void setHologram(IHolo hologram);

    void setText(String text);

    void setText(String text, boolean update);

    void setText(Component text);

    void setText(Component text, boolean update);

    /**
     * Respawn this line for every viewer.
     */
    void reveal();

    /**
     * Respawn this line for one viewer.
     */
    void reveal(Player player);

    /**
     * Hide this line without dropping it from the hologram.
     */
    void remove();

    /**
     * Hide this line for one viewer.
     */
    void remove(Player player);

    /**
     * Hide this line and remove it from the hologram.
     */
    void destroy();

    /**
     * Packet entity id of this line (armor stand or text display).
     */
    int getEntityId();
}
