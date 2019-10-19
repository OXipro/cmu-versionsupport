### Sound Support

```xml
<repository>
  <id>version-support</id>
  <url>https://gitlab.com/api/v4/projects/14877570/packages/maven</url>
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
        String version = Bukkit.getServer().getClass().getName().split(".")[3];
        Class c;
        try {
            c = Class.forName("com.andrei1058.spigot.versionsupport.sound." + version);
        } catch (ClassNotFoundException e) {
            //I can't run on your version
            return;
        }
        SoundSupport soundSupport = c.getConstructors()[0].newInstance();
    }   
}
```

### Material Support

```xml
<repository>
  <id>version-support</id>
  <url>https://gitlab.com/api/v4/projects/14877570/packages/maven</url>
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
        String version = Bukkit.getServer().getClass().getName().split(".")[3];
        Class c;
        try {
            c = Class.forName("com.andrei1058.spigot.versionsupport.material." + version);
        } catch (ClassNotFoundException e) {
            //I can't run on your version
            return;
        }
        MaterialSupport materialSupport = c.getConstructors()[0].newInstance();
    }   
}
```

### Block Support

```xml
<repository>
  <id>version-support</id>
  <url>https://gitlab.com/api/v4/projects/14877570/packages/maven</url>
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