# Tanuki Launcher

Tanuki Launcher is a cross-platform launcher for the Touhou Project games.

The launcher uses a TUI interface, and is very simple to use and configure. You can add your games and game data (screenshots, etc) to your configuration list. It's very convenient to launch a game once your configuration is done.

You can also view screenshots.

I plan to add screenshot conversion and cropping, data backup, screen recording and more to Tanuki!

# Download

Download the latest instance of Tanuki in the [releases page](https://github.com/spacebanana420/tanuki/releases)

If you have Scala 3 in your system, download ```tanuki.jar```

If you just have Java in your system, download ```tanuki-java.jar```

# Requirements & how to use

Tanuki requires the following to work:
* Scala 3 or Java 19
* FFmpeg (optional, for screenshot functionality)

Launcher's support extends to all operating systems with WINE support + Windows.

Depending on the version of Tanuki you downloaded, you can launch it with the commands ```scala``` or ```java -jar```

To play games and manage your data, you need to add game and data entries to your ```config.txt```, which is automatically created where the jar is.

You can configure this file from within the launcher, or you can manually write your config text file.

Lines in the config file that start with "#" or don't start with an entry option are ignored.

### Supported options:

* ```game``` - Add a game entry, in the format of this style: ```game=name:/path/to/game.exe```
  * The path must lead to a file
* ```data``` - Add a data entry, for where your screenshots, replays, scorefiles, etc are: ```data=name:/path/to/data/```
  * The path must lead to a directory
* ```command``` - Add a command entry to launch the games with: ```command=programname``` or ```command=/path/to/program```
  * The command does not support CLI arguments, it's just the program's name or the path to it.
  * Only 1 command is supported and is used for all game entries.
  * This is especially useful for **people running operating systems that are not Windows**, you can use WINE to launch Touhou.
* ```use_steam-run``` - Enables steam-run support. If you are not a NixOS user, ignore this option.
  * The package "steam-run" must be installed in your system.

### Config example:

```
command=wine

game=Touhou 7:/path/to/touhou 7/th7.exe
data=Touhou 7 Data:/path/to/touhou 7
```

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
scalac src/*.scala src/*/*.scala src/*/*/*.scala -d tanuki.jar
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
