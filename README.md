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
        SoundSupport soundSupport = SoundSupport.SupportBuilder.load();
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
        MaterialSupport materialSupport = MaterialSupport.SupportBuilder.load();
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