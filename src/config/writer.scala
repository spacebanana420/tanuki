package tanuki.config

import tanuki.system_platform
import tanuki.Platform

import java.io.File
import java.io.FileOutputStream

def defaultTanukiConfig(enable_wine: Boolean): String =
  val wine_str =
    if enable_wine then
      "\n\n#wine=wine\n#As it's necessary to run Windows games with WINE for your system, disabling this setting just defaults it to 'wine' anyways"
    else
      ""

  "# Tanuki Launcher's main configuration file\n# All lines that start with '#' are ignored by Tanuki. To uncomment said lines, you just have to remove the character."
  + "\nThe example settings below are a fraction of all settings supported by Tanuki. Check the online documentation for more information at https://github.com/spacebanana420/tanuki/blob/main/doc/config.md\n\n"
  + "#game=Name:/path/to/game\n#Example of setting a game entry"
  + "\n\n#data=Name:/path/to/touhou_game_data\n#This should lead to the directory where you would find your Touhou game's replays, snapshots, scorefile, etc"
  + "\n\n#game_cmd=firefox https://github.com\n#As an alternative to executing games, you can also run commands"
  + wine_str

def createConfig() = 
  val config = FileOutputStream("config.txt")
  val default_setup =
    if system_platform != Platform.Windows then defaultTanukiConfig(true)
    else defaultTanukiConfig(false)
  config.write(default_setup.getBytes())

def writeConfig(cfg: Seq[String], append: Boolean = true) =
  def createStr(str: String = "", i: Int = 0): String =
    if i >= cfg.length then
      str
    else if cfg(i) == "" then
      createStr(str, i+1)
    else
      createStr(str + cfg(i) + '\n', i+1)

  val cfgstr = createStr()
  if cfgstr != "" then
    FileOutputStream("config.txt", append).write(cfgstr.getBytes())
