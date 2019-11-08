XYC
====
XYC is a library containing a rich set of features, mainly intended for Minecraft use. 

**Note:** Even though there are some relatively recent parts,
certain legacy components haven't been significantly refactored
since the project's inception in 2013. These may expose dirty 
APIs and usually don't follow clean code guidelines.

Modules
--------
### xyc-core
This module contains core functionality which doesn't depend on anything Minecraft-related - It can therefore be used in any kind of Java project.
It includes some annotations, an API for checklists, a few Collection-related classes, Mojang's AuthLib, a (not totally efficient) MySQL connection
thingy as well as a class for determining jar versions and a few miscellaneous utilities.

3rd-party libraries included:
 - [Mojang's AuthLib](http://github.com/Mojang/AuthLib) (in source; modified)
 - [Google Guava v17](https://code.google.com/p/guava-libraries/) (shaded; Bukkit/Spigot include outdated versions)
 - [Trove4j](http://trove.starlight-systems.com/) (shaded; for CaseInsensitiveMap)
 - [Google Gson](https://code.google.com/p/google-gson/) (shaded; for AuthLib)

### xyc-bukkit
This module contains utilities for Bukkit, such as a locale and help framework (both not really up-to-date), a registry
for UUID providers, several classes which make performing actions (such as opening/closing inventories, updating signs, etc.)
on Bukkit's main thread easier, an extension framework for Bukkit's JavaPlugin class, offering easier SQL integration and more,
as well as several utility classes.

Obviously, this requires you to have Bukkit or Spigot-API in your classpath.

Oh yeah, and you can run this as a Bukkit plugin to avoid shading everything into your plugin.

### xyc-bungee
This module currently only provides a single utility class for BungeeCord, but is probably going to be extended in the
future. You need BungeeCord-API in your classpath to use this.

### xyc-bukkit-test
This module contains some helpful utilities for testing Bukkit plugins. You need xyc-bukkit, JUnit 4 and Mockito in your
classpath to be able to use this.

### xyc-games
This module contains some utilities for creating custom minigames for Bukkit, such as a player data framework (needs
improvement), a framework for saving and loading kits from YAML files, some classes for managing teams, as well as
a Bukkit task that lets you easily teleport people to another location while making sure that they don't move, etc.

You need xyc-bukkit in your classpath for this to work.

You can run this as a Bukkit plugin too if you want. (Use the standalone distribution)

Documentation
-------------
You can build documentation yourself using Maven:
````
mvn javadoc:aggregate
````
On the other hand, you could find JavaDocs [at my CI server](https://ci.lit.plus/job/literalplus/job/xyc/job/master/Javadocs/)

Compilation
-----------
This project uses Apache Maven for compilation.

````
mvn clean install
````
You can also find pre-built artifacts [at my CI server](https://ci.lit.plus/job/literalplus/job/xyc/job/master/).

Maven artifact descriptor:

````xml
<repositories>
  <repository>
    <id>xxyy-repo</id>
    <url>http://repo.lit.plus/repo/xxyy-lib/</url>
  </repository>
</repositories>
<dependencies>
  <dependency>
    <groupId>li.l1t.common</groupId>
    <artifactId>xyc-[[your module]]</artifactId>
    <version>[[See pom.xml]]</version>
  </dependency>
</dependencies>
````

Versioning
----------
XYC uses a `a.y.x.z` semantic versioning scheme.

`a` is the overall version. This gets bumped when the project structure is significantly changed.

`x` is the major version. This gets bumped when considerable new APIs are introduced, or serious binary incompatibilities are introduced.

`y` is the minor version. This gets bumped for new features and considerable changes. Small binary incompatibilities may arise.

`z` is the bugfix version. This gets bumped for bugfixes.

Support
-------

Just open an issue on GitHub.

License
-------

This project is licensed under a MIT License.
See the `LICENSE` file for details.
