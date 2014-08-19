XYC
====
XYC is a library containing a rich set of features, mainly intended for Minecraft use. 

Modules
--------
### xyc-core
This module contains core functionality which doesn't depend on anything Minecraft-related - It can therefore be used in any kind of Java project.
It includes some annotations, an API for checklists, a few Collection-related classes, Mojang's AuthLib, a (not totally efficient) MySQL connection
thingy as well as a class for determining jar versions and a few miscellaneous utilities.

3rd-party libraries included:
 - [Mojang's AuthLib](http://github.com/Mojang/AuthLib) (in source)
 - [Google Guava v17](https://code.google.com/p/guava-libraries/) (shaded; Bukkit/Spigot include outdated versions)
 - [Trove4j](http://trove.starlight-systems.com/) (shaded; for CaseInsensitiveMap)
 - [Google Gson](https://code.google.com/p/google-gson/) (shaded; for AuthLib)

### xyc-bukkit
This module contains utilities for Bukkit, such as a locale and help framework (both not really up-to-date), a registry
for UUID providers, several classes which make performing actions (such as opening/closing inventories, updating signs, etc.)
on Bukkit's main thread easier, an extension framework for Bukkit's JavaPlugin class, offering easier SQL integration and more,
as well as several utility classes.

Obviously, this requires you to have Bukkit or Spigot-API in your classpath.

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

Documentation
-------------
You can build documentation yourself using Maven:
````
mvn javadoc:aggregate
````
On the other hand, you could find JavaDocs [from my CI server](http://server.nowak-at.net/jenkins/job/public~XYC-Deploy/javadoc/)

Compilation
-----------
This project uses Apache Maven for compilation.
````
mvn clean install
````
You can also find pre-built artifacts [at my CI server](http://server.nowak-at.net/jenkins/job/xyca~XYC_compile/).

If you want to use XYC in a project, try this:
*Add to your `<repositories>` section:* (Warning: You need special access to my repo for this)
````xml
<repository>
  <id>xxyy-repo</id>
  <url>http://repo.nowak-at.net/repo/xxyy-lib/</url>
</repository>
````
*Add to your `<dependencies>` section:*
````xml
<dependency>
  <groupId>io.github.xxyy.common</groupId>
  <artifactId>xyc-[[your module]]</artifactId>
  <version>[[See pom.xml]]</version>
</dependency>
````

Support
-------
Join my IRC channel [#lit on irc.spi.gt](http://irc.spi.gt/iris/?channels=lit), ask your question and patiently wait for help.

License
-------
````
Copyright (c) 2013-2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.

Any usage, including, but not limited to, compiling, running, redistributing, printing,
 copying and reverse-engineering is strictly prohibited without permission from the
 original author and may result in legal steps being taken.
 
See the LICENSE file for details.
````
