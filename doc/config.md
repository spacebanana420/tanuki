# Tanuki Configuration

To play games and manage your data, you need to add game and data entries to your ```config.txt```, which is automatically created where the jar is.

You can configure this file from within the launcher, or you can manually write your config text file.

Lines in the config file that start with "#" or don't start with an entry option are ignored.

### Entry settings
* ```game``` - Add a Touhou game entry.
  * The path must lead to a file.
* ```data``` - Add a data entry, for where your screenshots, replays, scorefiles, etc are.
  * The path must lead to a directory.
* ```game_cmd``` - Command entry, as alternative to running game entries.
  * Commands are not whitespace-safe, and using quotation marks doesn't make it safe.

### Common global settings
* ```runner``` - Adds a command to launch all games with.
  * The command does not support multiplte CLI arguments, it's just the program's name or the path to it.
  * Only 1 command is supported and is used for all game entries.
* ```sidecommand_start``` - Command that runs before launching a game.
* ```sidecommand_close``` - Command that runs after returning to main menu in the game screen.
* ```use_steam-run``` - Enables steam-run support. If you are not a NixOS user, ignore this option.
  * The package "steam-run" must be installed in your system.
* ```return_closegame``` - If set to "yes" or "true", then the game will close if you "press enter to return to the main menu"
  * Some games will refuse to close, such as the Touhou bullet hell games with Thcrap under WINE
* ```ffmpeg_path``` - Sets the path to look for the ffmpeg executable
* ```ffplay_path``` - Sets the path to look for the ffplay executable

### Screenshot settings
* ```screenshot_format``` - The format to encode Tanuki screenshots into.
  * Supported formats are `png`, `jpg` and `avif`. Defaults to png.
* ```screenshot_delay``` - The delay (in milliseconds) for Tanuki to take a screenshot.
  * Disabled by default.
* ```screenshot_path``` - The path to save Tanuki screenshots into.
  * If the path doesn't point to a directory with write permissions, Tanuki's directory is chosen.
* ```screenshot_jpg_quality``` - Sets the quality for JPG screenshots, if `screenshot_format` is set to "jpg".
  * Supported values range between 1 and 20. Lower value means better quality and bigger files. 1 and 2 are generally recommended.
  * Default value is 1.
* ```screenshot_png_quality``` - Sets the quality for PNG screenshots, if `screenshot_format` is set to "png".
  * Supported values: "low", "medium" and "high".
  * PNG is lossless, and so "quality" here means compression efficiency and not picture quality.
  * Default value is "high".
  
### WINE settings
* ```wine``` - The path for the WINE build to launch games with
* ```wine_prefix``` - Sets which directory to use as your WINE prefix
  * The prefix directory must already exist
* ```dxvk_framerate``` - Sets a framerate limit with DXVK
  * This only works if your WINE prefix has DXVK enabled, Tanuki does not configure this

The ```game``` and ```data``` entries require a name and a path, separated by ```:```. The order of the entries does not matter.

Some options, like ```command``` and ```wine``` are only read once, while multiple entries of options like ```game``` are supported.

```wine``` is used for running Windows games on non-Windows systems, while ```command``` is for a custom program to launch games/files/programs with.

```sidecommand_start``` and ```sidecommand_close``` run in parallel to game execution and closure and are not whitespace-safe, but you can pass CLI arguments to them and so build a full command.

```ffmpeg_path``` can be used to set a custom directory where the executables for FFmpeg and FFplay are. This path must be a directory, this way this single setting can be used to detect both FFmpeg and FFplay. If the path doesn't have the executables or is not a directory, the default system FFmpeg and FFplay are used (if they are installed and in your $PATH).

### Config example:

```
wine=wine

game=Touhou 7:/path/to/touhou 7/th7.exe
data=Touhou 7 Data:/path/to/touhou 7
```

The ```game``` and ```data``` entries require a name and a path, separated by ```:```. The order of the entries does not matter.

## Running on Linux, MacOS, FreeBSD, etc

Tanuki is cross-platform, but the Touhou games are only distributed for x86 Windows. If you are not running Windows, you can run Touhou with WINE.

To run your Touhou games with WINE, add the following setting to your config.txt:
```
wine=wine
```
This setting will make Tanuki run your games with your system's wine. Note that it must then be installed. You can also add a custom wine fork that you downloaded by specifying the path to it.

I personally recommend you download and use [Wine-GE](https://github.com/GloriousEggroll/wine-ge-custom) instead, as it has better audio resampling (in-game SFX audio won't be muffled/distorted) and it uses fshack by default, which prevents the game's resolution from affecting your desktop's.

If you download a custom WINE build, assuming the path to your custom wine build is "/path/to/custom-wine/bin/wine", you can add to your config.txt:
```
wine=/path/to/custom-wine/bin/wine
```

You can also specify a custom WINE prefix with this setting:
```
wine_prefix=/path/to/.wine_prefix
```

## Running on NixOS

Custom WINE builds are linked against glibc and other libraries that are located in common Linux paths. NixOS, however, does not use these traditional paths, making these binaries incompatible by default.

To run a custom WINE build on NixOS, install ```steam-run``` on your system and add the following setting to your config.txt:
```
use_steam-run=true
```

## Bad input delay with Xfce and other desktops

It seems that the Xfce desktop does not automatically disable or bypass its compositor and desktop vsync when a fullscreen game is launched. It lets you, however, manually disable it, either through the GUI settings or through a command.

With this following configuration, Xfce's compositor will disable when you start a game and re-enable once you come back to the main menu:

```
sidecommand_start=xfconf-query -c xfwm4 -p /general/use_compositing -s false
sidecommand_close=xfconf-query -c xfwm4 -p /general/use_compositing -s true
```

These commands only apply to Xfce, though it's likely that your desktop has its own commands for disabling the compositor, or it automatically disables when a fullscreen game is opened.

This is not necessary or possible to do under Wayland, but a proper Wayland implementation will automatically disable vsync and effects for fullscreen apps.

<p align="center">
<img src="../images/mamizou.png" height="230"/>
</p>
