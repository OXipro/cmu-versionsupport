[![Discord Server](https://discord.com/api/guilds/201345265821679617/widget.png)](https://discord.gg/XdJfN2X)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/98b1acd95dba4ab1a32781d048160a81)](https://www.codacy.com/manual/andrew.dascalu/spigotversionsupport/dashboard?utm_source=gitlab.com&amp;utm_medium=referral&amp;utm_content=andrei1058/spigotversionsupport&amp;utm_campaign=Badge_Grade)
[![Jenkins](https://img.shields.io/badge/Download-Jenkins-green)](https://ci.codemc.io/job/andrei1058/job/SpigotVersionSupport/)
[![JavaDocs](https://img.shields.io/badge/Docs-Javadocs-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport)
[![Fork](https://img.shields.io/badge/Fork%20me%20on-GitLab-yellow)](https://gitlab.com/andrei1058/spigotversionsupport)
# Spigot Version Support API
This API provides various NMS based methods and spigot extension for deprecated or modified methods in order to keep backwards compatibility for your plugin. It does not rely on Reflection :)

## Maven Repository
```xml
<repository>
  <id>andrei1058-releases</id>
  <url>http://repo.andrei1058.com/releases</url>
</repository>
```  
or for development builds:  
```xml
<repository>
  <id>andrei1058-snapshots</id>
  <url>http://repo.andrei1058.com/snapshots</url>
</repository>
``` 
<br/>  

### Sound Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Sounds-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/sounds-version)
##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>sounds-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static SoundSupport soundSupport;

    public void onEnable(){
        // loading support
        soundSupport = SoundSupport.SupportBuilder.load();

        // server version not supported
        if (soundSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        // check if given sound is an existing sound of current server version
        soundSupport.isSound("checkThis");
        // get sound for current server version
        // will return sound1.8 for 1.8 server, sound1.12 for version in range [1.9,12] 
        // and 1.13 for 1.13 and newer
        soundSupport.getForCurrentVersion("sound1.8", "sound1.12", "sound1.13");
    }   
}
```
<br/> 

### Material Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Material-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/material-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>material-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static MaterialSupport materialSupport;

    public void onEnable(){
        // loading support
        materialSupport = MaterialSupport.SupportBuilder.load();

        // server version not supported
        if (materialSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        materialSupport.isWool(event.getItemStack().getMaterial());
        materialSupport.isBed(event.getBlock().getMaterial());
        materialSupport.isTerracotta(event.getBlock().getMaterial());
        materialSupport.isGlassPane(event.getBlock().getMaterial());
        materialSupport.isGlass(event.getBlock().getMaterial());
        materialSupport.isConcrete(event.getBlock().getMaterial());
        materialSupport.isConcretePowder(event.getBlock().getMaterial());
        materialSupport.isCake(event.getBlock().getMaterial());
        materialSupport.isMaterial(event.getBlock().getMaterial());
        materialSupport.isSoil(event.getBlock().getMaterial());
        // returns v1_8 if server version is 1.8, returns v1_12 if
        // the server version is in range [1.9,1.12] otherwise 1.13
        materialSupport.getForCurrent(v1_8, v1_12, v1_13);
    }   
}
```

### Block Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Block-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/block-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>block-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static BlockSupport blockSupport;

    public void onEnable(){
        // loading support
        blockSupport = BlockSupport.SupportBuilder.load();

        // server version not supported
        if (blockSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        // usually used to set block colors. changes will be applied on versions older than 1.13
        blockSupport.setBlockData(event.getBlock(), 4);
        // change a block durability (on the entire server)
        // blockName13 is used for 1.13 or newer.
        blockSupport.setDurability("blockName1.8", "blockName13", 12f);
        blockSupport.getBlockBehindSign(signBlock);
    }   
}
```

### ItemStack Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-ItemStack-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/itemstack-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>itemstack-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static ItemStackSupport itemStackSupport;

    public void onEnable(){
        // loading support
        itemStackSupport = ItemStackSupport.SupportBuilder.load();

        // server version not supported
        if (itemStackSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        itemStackSupport.getInHand(player);
        // always null on 1.8
        itemStackSupport.getInOffHand(player);
        itemStackSupport.createItem(material, amound, data);
        // add NBT tag
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        item = itemStackSupport.addTag(item, "key", "stringValue");
        // check if an item stack has the given NBT tag
        itemStackSupport.hasTag(item, "key");
        itemStackSupport.getTag(item, "key");
        item = itemStackSupport.removeTag(item, "key");
        // make an item unbreakable
        item = itemStackSupport.setUnbreakable(item);
        // will take the given amount of the given type from player's inventory
        // usually used for shop transactions
        itemStackSupport.minusAmount(player, item, 1);
        // check how much damage does the given item.
        itemStackSupport.getDamage(item);
        itemStackSupport.isArmor(item);
        itemStackSupport.isTool(item);
        itemStackSupport.isSword(item);
        itemStackSupport.isAxe(item);
        itemStackSupport.isBow(item);
        itemStackSupport.isProjectile(item);
        itemStackSupport.isPlayerHead(item);
        itemStackSupport.applyPlayerSkinOnHead(player, null);
        // get item data for 1.12 or older
        itemStackSupport.getItemData(item);
        // put custom texture on player head
        item = itemStackSupport.applySkinTextureOnHead("eb28f4eeff891b78d51f75d8722c628484ba49df9c9f2371898c26967386", null);
    }   
}
```

### Title/ Action messages Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Title-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/title-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>title-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static TitleSupport titleSupport;

    public void onEnable(){
        // loading support
        titleSupport = TitleSupport.SupportBuilder.load();

        // server version not supported
        if (titleSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        titleSupport.sendTitle(player, "title", "sub title", fadeIn, stay, fadeOut);
        titleSupport.playAction(player, "message");
    }   
}
```

### Chat messages Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Chat-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/chat-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>chat-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static ChatSupport chatSupport;

    public void onEnable(){
        // loading support
        chatSupport = ChatSupport.SupportBuilder.load();

        // server version not supported
        if (chatSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        chatSupport.sendMessage(commandSender, textComponent);
        
        // send multiple lines at one. 
        // it may seem stupid but spigot didn't provide those methods in older versions.
        TextComponent[] lines = new TextComponent[]{new TextComponent("a"), new TextComponent("b")};
        chatSupport.sendMessage(commandSender, lines);
        
        // append
        ComponentBuilder componentBuilder = new ComponentBuilder("TEXT");
        chatSupport.append(componentBuilder, new TextComponent("something"));
        
        chatSupport.sendMessage(commandSender, componentBuilder);
        chatSupport.sendMessage(commandSender, new CompinentBuilder[]{componentBuilder,componentBuilder});
    }   
}
```

### Command Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Command-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/cmd-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>cmd-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static CommandSupport cmdSupport;

    public void onEnable(){
        // loading support
        cmdSupport = CommandSupport.SupportBuilder.load();

        // server version not supported
        if (cmdSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        cmdSupport.registerCommand(this, "name", new MyCommand());
    }   
}
```

### Particle Effects Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-Particle-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/particle-version)

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>particle-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static ParticleSupport particleSupport;

    public void onEnable(){
        // loading support
        particleSupport = ParticleSupport.SupportBuilder.load();

        // server version not supported
        if (particleSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        particleSupport.spawnParticle(world, x, y, z, "particleName");
        // amount in certain cases corresponds to particle color
        particleSupport.spawnParticle(world, "particleName", x, y, z, offsetX, offsetY, offsetZ, speed, amount);
        // returns V18 if server version is 1.8, returns V19 for 1.9, V12 for [1.10,1.12] and V13 for 1.13 and newer.
        particleSupport.getForVersion(world, "v18", "v19", "V12", "V13");
    }   
}
```

### Player NPC Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-PlayerNPC-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/player-npc-version)

Supports 1.12+. Create Player entities (not fake).

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>player-npc-version</artifactId>
  <version>[1.5.0,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static PlayerNPCSupport playerNPCSupport;

    public void onEnable(){
        // loading support
        playerNPCSupport = PlayerNPCSupport.SupportBuilder.load();

        // server version not supported
        if (playerNPCSupport == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // demo of a few methods. check all of them on javadocs
        // returns player
        playerNPCSupport.spawnNPC(Location location, GameProfile gameProfile);
        // returns player
        playerNPCSupport.spawnNPC(Location location, Player player, boolean copyArmor);
    }   
}
```

### Player Utils Support
[![JavaDocs](https://img.shields.io/badge/JavaDocs-PlayerNPC-orange)](http://javadocs.andrei1058.com/SpigotVersionSupport/player-npc-version)

Supports 1.8.8+. An interface to support modified player methods through the versions.

##### Dependency
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>player_utils_version</artifactId>
  <version>[1.5.5,)</version>
  <scope>compile</scope>
</dependency>
```

##### How to use
```java
public class Main extends JavaPlugin {

    private static PlayerUtilsSupport playerVersionedUtils;

    public void onEnable(){
        // loading support
        playerVersionedUtils = PlayerUtilsSupport.SupportBuilder.load();

        // server version not supported
        if (playerVersionedUtils == null){
            getLogger().severe("Server version not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // hide a player to a player (receiver)
        playerVersionedUtils.hidePlayer(Player toBeHidden, Player receiver, Plugin requester);
        // un-hide a player to a player
        playerVersionedUtils.unHidePlayer(Player toBeShown, Player receiver, Plugin requester);
    }   
}
```