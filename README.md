# CMU Version Support

Java library for Bukkit plugins. One API across Minecraft 1.8.8 through 26, on Spigot and on Paper.

Your plugin calls `SupportBuilder.load()`. That call picks the implementation for the running server.

Maven multi-module project, group `com.oxipro.cmu.versionsupport`, version `1.7.2`. License: AGPL-3.0.

## Modules

Depend on the shaded jar for the area you use. It contains the interface and every version implementation.

`block_version`, `itemstack_version`, `sounds_version`, `material-version`, `chat-version`, `title_version`, `particle_version`, `player_utils_version`, `entity_utils_version`, `hologram_version`, `server_version`, `world_version`, `cmd_version`, `player_npc_version`.

## Version detection

Spigot 1.8 through 1.21 still puts the NMS token in the CraftBukkit package, for example `v1_8_R3` or `v1_21_R7`. That token wins.

Paper from 1.20.5, and Spigot 26, use `org.bukkit.craftbukkit` with no token. The loader then reads `Bukkit.getBukkitVersion()`.

1.8 forks share one implementation. Spigot, CraftBukkit, Paper, PandaSpigot, TacoSpigot, NachoSpigot, SportPaper, and FlamePaper on 1.8.8 all resolve to `v1_8_R3`.

From Minecraft 26, `26.1` is the base token `v26`. A later release looks for its own class first. If nothing in that class changed, it uses `v26`.

`load()` returns null when the version is unknown.

## Init

Create it once from `onEnable`.

```java
BlockSupport blocks = BlockSupport.SupportBuilder.load();
HologramSupport holograms = HologramSupport.SupportBuilder.load();
EntityUtilsSupport entities = EntityUtilsSupport.SupportBuilder.load();
```

Null means this server version has no implementation for that area.

## Maven

```xml
<dependency>
    <groupId>com.oxipro.cmu.versionsupport</groupId>
    <artifactId>block_version</artifactId>
    <version>1.7.2</version>
</dependency>
```

Run `mvn install` in this repo first. The jars are not on a public repository. JDK 21 or newer to build the full project. Version detection itself is Java 11.
