# Realistic minecart wind effect

Experience the immersive sensation of wind rushing past as you travel in a minecart.

As you ride along the tracks, you'll hear the distinct sound of the wind, 
complete with variations in intensity based on your speed and surrounding environment.

## Key Features:

- Realistic wind sound effects in stereo, surrounding you with an immersive audio experience.
- Dynamic intensity adjustments as you travel, creating a sense of speed and environmental interaction.
- Elevate your gameplay with unparalleled realism and ambiance.

## Compile plugin project 🚀
- Install Git, Java 17 and Maven
- Clone this repository
- If you are using IntelliJ Idea:
  - Open the repository folder as project
  - At right, open `Maven`
    - Click `Download sources and documentation` to download spigot API and their dependencies (this phase could have
been executed automatically by IntelliJ the first time you open the project)
    - Expand Lifecycle and click `package` to compile the plugin
- If you are using the terminal (not fully tested)
  - Run one terminal inside repository folder 
  - Run `mvn dependency:resolve` to download spigot API and their dependencies
  - Run `mvn package` to compile de plugin
- To change the plugin output jar when you compile it, simply uncomment and set
`<outputDirectory>../devServer/plugins</outputDirectory>` in `pom.xml` but **NEVER** commit it to git

## Set up server 🌍
- Create a new folder
- Download and put `BuildTools.jar` inside that folder
- Run `java -jar BuildTools.jar`
- Run `java -jar spigot-1.1x.x.jar` where `xx` is the minecraft server version
  - Set `eula.txt` text to `true` and re-run previous command if needed
- Optional
  - Go to the previous created plugin project and set in `pom.xml` the route to plugins folder into
`<outputDirectory>../devServer/plugins</outputDirectory>`

## Debug plugin and server 🧑‍💻
We need to open a debug port to debug the server, and then listen it from IntelliJ.

In IntelliJ:
- Right to hammer button, click Edit configurations
- Add `Remote JVM Debug` and full copy the `agentlib` command line parameter

In the server launcher script:
- Add the copied param to java command:
  - RESULT: `java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 spigot-1.xx.jar --nogui`
- Start the minecraft server with the above script

In IntelliJ:
- Press the bug button 🪲 to listen to server
- Then you can set debug points in your plugin code.
