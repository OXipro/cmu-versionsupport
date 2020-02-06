### Sound Support

```xml
<repository>
  <id>andrei1058-repo</id>
  <url>http://repo.andrei1058.com/releases</url>
</repository>
```
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>sounds-version</artifactId>
  <version>[1.0,)</version>
</dependency>
```

```java
public class Main extends JavaPlugin {
    public void onEnable(){
        SoundSupport soundSupport = SoundSupport.SupportBuilder.load();
    }   
}
```

### Material Support

```xml
<repository>
  <id>andrei1058-repo</id>
  <url>http://repo.andrei1058.com/releases</url>
</repository>
```
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>material-version</artifactId>
  <version>[1.0,)</version>
</dependency>
```

```java
public class Main extends JavaPlugin {
    public void onEnable(){
        MaterialSupport materialSupport = MaterialSupport.SupportBuilder.load();
    }   
}
```

### Block Support

```xml
<repository>
  <id>andrei1058-repo</id>
  <url>http://repo.andrei1058.com/releases</url>
</repository>
```
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>block-version</artifactId>
  <version>[1.0,)</version>
</dependency>
```

```java
public class Main extends JavaPlugin {
    public void onEnable(){
        BlockSupport blockSupport = BlockSupport.SupportBuilder.load();
    }   
}
```

### ItemStack Support

```xml
<repository>
  <id>andrei1058-repo</id>
  <url>http://repo.andrei1058.com/releases</url>
</repository>
```
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>itemstack-version</artifactId>
  <version>[1.0,)</version>
</dependency>
```

```java
public class Main extends JavaPlugin {
    public void onEnable(){
        ItemStackSupport itemStackSupport = ItemStackSupport.SupportBuilder.load();
    }   
}
```

### Title/ Action messages Support

```xml
<repository>
  <id>andrei1058-repo</id>
  <url>http://repo.andrei1058.com/releases</url>
</repository>
```
```xml
<dependency>
  <groupId>com.andrei1058.spigot.versionsupport</groupId>
  <artifactId>title-version</artifactId>
  <version>[1.0,)</version>
</dependency>
```

```java
public class Main extends JavaPlugin {
    public void onEnable(){
        TitleSupport titleSupport = TitleSupport.SupportBuilder.load();
    }   
}
```