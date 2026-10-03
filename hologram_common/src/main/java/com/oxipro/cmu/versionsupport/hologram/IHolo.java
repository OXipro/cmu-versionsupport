package com.oxipro.cmu.versionsupport.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public interface IHolo {

    Set<Player> getPlayers();

    void addPlayer(Player player);

    void removePlayer(Player player);

    void addLine(IHoloLine line);

    void removeLine(IHoloLine line);

    void removeLine(int index);

    /**
     * Removes the first line whose legacy text contains {@code text}.
     */
    void removeLineContaining(String text);

    void clearLines();

    void update();

    void update(Player player);

    void show();

    void hide();

    boolean isShowing();

    int size();

    IHoloLine getLine(int index);

    List<IHoloLine> getLines();

    Location getLocation();

    void setLines(String[] lines, boolean update);

    void setLines(Component[] lines, boolean update);

    void setLines(List<IHoloLine> lines, boolean update);

    void setLine(int index, IHoloLine line);

    void setLine(int index, IHoloLine line, boolean update);

    void setLine(int index, String line);

    void setLine(int index, String line, boolean update);

    void setLine(int index, Component line);

    void setLine(int index, Component line, boolean update);

    void insertLine(int index, IHoloLine line);

    void insertLine(int index, IHoloLine line, boolean update);

    void insertLine(int index, String line);

    void insertLine(int index, String line, boolean update);

    void insertLine(int index, Component line);

    void insertLine(int index, Component line, boolean update);

    void addLine(IHoloLine line, boolean update);

    void addLine(String line);

    void addLine(String line, boolean update);

    void addLine(Component line);

    void addLine(Component line, boolean update);

    void removeLine(IHoloLine line, boolean update);

    void removeLine(int index, boolean update);

    void clearLines(boolean update);

    void setGap(double gap);

    void setGap(double gap, boolean update);

    double getGap();

    void remove();

    /**
     * Mount this hologram on {@code entity}. Offset is extra height above the ride attachment point.
     */
    void attach(Entity entity);

    /**
     * Mount this hologram on {@code entity}. {@code yOffset} is extra height above the ride attachment point.
     * On 1.8 the offset is unused (armor-stand ride chain).
     */
    void attach(Entity entity, double yOffset);

    /**
     * Stop riding the entity. Lines stay spawned at {@link #getLocation()}.
     */
    void detach();

    Entity getAttachedEntity();

    double getAttachOffset();

    void setAttachOffset(double yOffset);

    /**
     * Re-send the mount packet. Call after teleport, respawn, or real passenger changes.
     */
    void refreshAttach();
}
