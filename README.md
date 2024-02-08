# Tanuki Launcher

Tanuki Launcher is a cross-platform launcher for the Touhou Project games.

The launcher uses a TUI interface, and is very simple to use and configure. You can add your games and game data (screenshots, etc) to your configuration list. It's very convenient to launch a game once your configuration is done.

You can also view screenshots.

#### This project is very newborn and the base functionality is unfinished.

# Download

There are no releases yet to download.

# Requirements & how to use

Tanuki requires the following to work:
* Scala 3 or Java 19
* FFmpeg (optional, for screenshot functionality)

Launcher's support extends to all operating systems with WINE support + Windows.

Documentation here is unfinished for now.

## Running on Linux, MacOS, FreeBSD, etc

Tanuki is cross-platform, although the Touhou games are distributed for the Windows operating system. If you are not running Windows, you can run Touhou with WINE.

To run your Touhou games with WINE, add the following setting to your config.txt:
```
command=wine
```
I personally recommend you use Wine-GE, as it has better audio resampling (SFX won't be muffled in-game) and it uses fshack by default, which prevents the game's resolution from affecting your desktop's.

If you download a custom WINE build, you can use it to launch your games with its absolute path. Assuming the path to your custom wine build is "/path/to/custom-wine/bin/wine", you can add to your config.txt:
```
command=/path/to/custom-wine/bin/wine
```

## Running on NixOS

Custom WINE builds are linked against glibc and other libraries that are located in common Linux paths. NixOS, however, does not use these traditional paths, making these binaries incompatible by default

To run a custom WINE build on NixOS, install ```steam-run``` on your system and add the following setting to your config.txt:
```
use_steam-run=true
```


# Compile from source

You need Scala 3 to build Tanuki from source. You can use the scalac compiler or scala-cli

## Using scalac

```
scalac src/*.scala src/*/*.scala -d tanuki.jar
```
You can now run this JAR with Scala or scala-cli

## Using scala-cli

### Option 1
```
scala-cli --power package src --library -o tanuki.jar
```
Only scala-cli can run this JAR

### Option 2
```
scala-cli --power package src --assembly --preamble=false -o tanuki-fat.jar
```
You can run this JAR with Java.

### Option 3
```
scala-cli compile src
```
The compiled software will be in a hidden folder inside "src". Run ```scala-cli src``` to run Tanuki.
