package com.oxipro.cmu.versionsupport.hologram.v1_8_R3;

import com.oxipro.cmu.versionsupport.hologram.IHolo;
import com.oxipro.cmu.versionsupport.hologram.IHoloLine;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hologram implements IHolo {

    private final Set<Player> players;
    private List<IHoloLine> lines;
    private final Location loc;
    private double gap = 0.25;
    private boolean showing = true;
    private org.bukkit.entity.Entity attached;
    private double attachOffset = 0.0;

    public Hologram(Player p, List<IHoloLine> lines, Location loc) {
        this.players = new HashSet<Player>();
        this.players.add(p);
        this.lines = lines;
        this.loc = loc;
    }

    public Hologram(Player p, Location loc, List<Component> lines) {
        this.players = new HashSet<Player>();
        this.players.add(p);
        this.loc = loc;
        this.lines = new ArrayList<IHoloLine>();

        for (Component line : lines) {
            this.lines.add(new HoloLine(line, this));
        }
    }

    public Hologram(Iterable<Player> players, List<Component> lines, Location loc) {
        this.players = new HashSet<Player>();
        for (Player p : players) this.players.add(p);
        this.lines = new ArrayList<IHoloLine>();
        this.loc = loc;

        for (Component line : lines) {
            this.lines.add(new HoloLine(line, this));
        }
    }

    public Hologram(Iterable<Player> players, Location loc, List<IHoloLine> lines) {
        this.players = new HashSet<Player>();
        for (Player p : players) this.players.add(p);
        this.lines = lines;
        this.loc = loc;
    }

    @Override
    public Set<Player> getPlayers() {
        return this.players;
    }

    @Override
    public void addPlayer(Player player) {
        if (!this.players.add(player)) return;
        for (IHoloLine line : this.lines) {
            line.reveal(player);
        }
        sendAttach(player, true);
    }

    @Override
    public void removePlayer(Player player) {
        this.players.remove(player);
        for (IHoloLine line : this.lines) {
            line.remove(player);
        }
    }

    @Override
    public void addLine(IHoloLine line) {
        this.lines.add(line);
    }

    @Override
    public void removeLine(IHoloLine line) {
        this.lines.remove(line);
    }

    @Override
    public void removeLine(int index) {
        IHoloLine line = this.lines.remove(index);
        if (line != null) {
            line.remove();
        }
    }

    @Override
    public void removeLineContaining(String text) {
        for (IHoloLine line : this.lines) {
            if (line.getText().contains(text)) {
                line.remove();
                break;
            }
        }
    }

    @Override
    public void clearLines() {
        for (IHoloLine line : this.lines) {
            line.remove();
        }
        this.lines.clear();
    }

    @Override
    public void update() {
        for (IHoloLine line : this.lines) {
            line.update();
        }
    }

    @Override
    public void update(Player player) {
        if (!this.players.contains(player)) return;
        for (IHoloLine line : this.lines) {
            line.update(player);
        }
    }

    @Override
    public void show() {
        this.showing = true;
        for (IHoloLine line : this.lines) {
            line.reveal();
        }
        refreshAttach();
    }

    @Override
    public void hide() {
        this.showing = false;
        sendAttachToViewers(false);
        for (IHoloLine line : this.lines) {
            line.remove();
        }
    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    @Override
    public int size() {
        return this.lines.size();
    }

    @Override
    public IHoloLine getLine(int index) {
        return this.lines.get(index);
    }

    @Override
    public List<IHoloLine> getLines() {
        return this.lines;
    }

    @Override
    public Location getLocation() {
        if (attached != null && attached.isValid()) {
            Location current = attached.getLocation();
            current.setY(current.getY() + attachOffset);
            return current;
        }
        return this.loc;
    }

    @Override
    public void setLines(String[] lines, boolean update) {
        Component[] components = new Component[lines.length];
        for (int i = 0; i < lines.length; i++) {
            components[i] = com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(lines[i]);
        }
        setLines(components, update);
    }

    @Override
    public void setLines(Component[] lines, boolean update) {
        clearLines();
        for (Component line : lines) {
            this.lines.add(new HoloLine(line, this));
        }
        if (update) {
            this.update();
        }
    }

    @Override
    public void setLines(List<IHoloLine> lines, boolean update) {
        clearLines();
        this.lines = lines;
        if (update) {
            this.update();
        }
    }

    @Override
    public void setLine(int index, IHoloLine line) {
        setLine(index, line, true);
    }

    @Override
    public void setLine(int index, IHoloLine line, boolean update) {
        IHoloLine previous = this.lines.set(index, line);
        if (previous != null && previous != line) {
            previous.remove();
        }
        if (update) {
            this.lines.get(index).update();
        }
    }

    @Override
    public void setLine(int index, String line) {
        setLine(index, com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(line), true);
    }

    @Override
    public void setLine(int index, String line, boolean update) {
        setLine(index, com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(line), update);
    }

    @Override
    public void setLine(int index, Component line) {
        setLine(index, line, true);
    }

    @Override
    public void setLine(int index, Component line, boolean update) {
        if (this.lines.isEmpty()) {
            this.lines.add(new HoloLine(line, this));
            return;
        }
        this.lines.get(index).setText(line, update);
    }

    @Override
    public void insertLine(int index, IHoloLine line) {
        this.lines.add(index, line);
    }

    @Override
    public void insertLine(int index, IHoloLine line, boolean update) {
        this.lines.add(index, line);
        if (update) {
            this.update();
        }
    }

    @Override
    public void insertLine(int index, String line) {
        insertLine(index, com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(line), true);
    }

    @Override
    public void insertLine(int index, String line, boolean update) {
        insertLine(index, com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(line), update);
    }

    @Override
    public void insertLine(int index, Component line) {
        insertLine(index, line, true);
    }

    @Override
    public void insertLine(int index, Component line, boolean update) {
        this.lines.add(index, new HoloLine(line, this));
        if (update) {
            this.update();
        }
    }

    @Override
    public void addLine(IHoloLine line, boolean update) {
        this.lines.add(line);
        if (update) {
            this.update();
        }
    }

    @Override
    public void addLine(String line) {
        addLine(com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(line), true);
    }

    @Override
    public void addLine(String line, boolean update) {
        addLine(com.oxipro.cmu.versionsupport.HologramSupport.fromLegacy(line), update);
    }

    @Override
    public void addLine(Component line) {
        addLine(line, true);
    }

    @Override
    public void addLine(Component line, boolean update) {
        this.lines.add(new HoloLine(line, this));
        if (update) {
            this.update();
        }
    }

    @Override
    public void removeLine(IHoloLine line, boolean update) {
        this.lines.remove(line);
        line.remove();
        if (update) {
            this.update();
        }
    }

    @Override
    public void removeLine(int index, boolean update) {
        IHoloLine line = this.lines.remove(index);
        if (line != null) {
            line.remove();
        }
        if (update) {
            this.update();
        }
    }

    @Override
    public void clearLines(boolean update) {
        clearLines();
        if (update) {
            this.update();
        }
    }

    @Override
    public void setGap(double gap) {
        this.gap = gap;
        this.update();
        refreshAttach();
    }

    @Override
    public void setGap(double gap, boolean update) {
        this.gap = gap;
        if (update) {
            this.update();
        }
    }

    @Override
    public double getGap() {
        return this.gap;
    }

    @Override
    public void remove() {
        sendAttachToViewers(false);
        for (IHoloLine line : this.lines) line.remove();
        lines.clear();
        attached = null;
    }

    @Override
    public void attach(org.bukkit.entity.Entity entity) {
        attach(entity, 0.0);
    }

    @Override
    public void attach(org.bukkit.entity.Entity entity, double yOffset) {
        if (entity == null) {
            detach();
            return;
        }
        this.attached = entity;
        this.attachOffset = yOffset;
        update();
        refreshAttach();
    }

    @Override
    public void detach() {
        sendAttachToViewers(false);
        this.attached = null;
        update();
    }

    @Override
    public org.bukkit.entity.Entity getAttachedEntity() {
        return attached;
    }

    @Override
    public double getAttachOffset() {
        return attachOffset;
    }

    @Override
    public void setAttachOffset(double yOffset) {
        this.attachOffset = yOffset;
        update();
        refreshAttach();
    }

    @Override
    public void refreshAttach() {
        sendAttachToViewers(true);
    }

    private void sendAttachToViewers(boolean includeHolo) {
        if (attached == null || !attached.isValid()) return;
        for (Player player : players) {
            sendAttach(player, includeHolo);
        }
    }

    private void sendAttach(Player player, boolean includeHolo) {
        if (attached == null || !attached.isValid()) return;
        Entity vehicle = ((CraftEntity) attached).getHandle();
        Entity previous = vehicle;
        if (includeHolo) {
            for (IHoloLine line : lines) {
                if (line.isDestroyed() || !(line instanceof HoloLine)) continue;
                Entity passenger = ((HoloLine) line).entity;
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                        new PacketPlayOutAttachEntity(0, passenger, previous));
                previous = passenger;
            }
        } else {
            for (IHoloLine line : lines) {
                if (!(line instanceof HoloLine)) continue;
                Entity passenger = ((HoloLine) line).entity;
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                        new PacketPlayOutAttachEntity(0, passenger, null));
            }
        }
    }
}
