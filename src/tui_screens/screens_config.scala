package tanuki.tui

import tanuki.system_platform
import bananatui.*
import tanuki.config.*
import java.io.File
import scala.sys.exit

def tui_configerror() =
  val text = "There's an error in your config.txt!\nYou might have a setting that isn't configured properly, or a game entry with a path that does not lead to a file, or a data entry with a path that does not lead to a directory!\n\nWould you like to configure Tanuki now and delete the old configuration file?"
  val answer = askPrompt(text, false)
  if !answer then
    println("Quitting Tanuki...")
    exit()
  else
    tui_configure(true)

def tui_noentries(entries: Seq[String]): Boolean =
  if entries.length == 0 then
    val text = "No entries have been found!\nWould you like to add some to your configuration?"
    val answer = askPrompt(text)
    if answer then
      tui_configure(false)
    true
  else
    false

def tui_configure(overwrite: Boolean) =
  def addGame(): String =
    val name = readUserInput("Type the name of your game entry to add (for example: Touhou 10)")
    val path = readUserInput("Type the full path to your game's executable")
    s"game=$name:$path"

  def addData(): String =
    val name = readUserInput("Type the name of your game entry to add (for example: Touhou 10 replays)")
    val path = readUserInput("Type the full path to your game's executable")
    s"data=$name:$path"

  def askSteamRun(): Boolean =
    if File("/nix/store").isDirectory() then
      val usesteamrun = askPrompt(s"It seems you are using NixOS\nIf you are running a custom third party wine build, it might not work out of the box\nWould you like to enable the use of steam-run to fix this?")
      if usesteamrun then
        true
      else
        false
    else
      false

  def makeOption(title: String, setting: String): String =
    val ans = readUserInput(title)
    if ans != "" then
      s"$setting=$ans"
    else
      ""

  def menu(cfg_settings: Vector[String] = Vector()): Vector[String] =
    val default_opt = if cfg_settings.length == 0 then "Cancel" else "Done"
    val choice = chooseOption(Vector("Game", "Data"),s"Choose the entry type to add", default_opt)
    choice match
      case 0 =>
        cfg_settings
      case 1 =>
        menu(cfg_settings :+ addGame())
      case 2 =>
        menu(cfg_settings :+ addData())
      case _ =>
        menu(cfg_settings)

  val cfg = menu()
  if cfg.length != 0 then
    val fullcfg =
      if overwrite then
        val command =
          val title = "Type the command/program to launch your native games with or leave it blank to disable"
          makeOption(title, "command")

        val wine =
          if system_platform != 0 then
            val title = "Type the command/path to the WINE helper or leave it blank to disable\nIf you have wine installed in your system, you can type \"wine\""
            makeOption(title, "wine")
          else ""

        val startcmd =
          val title = "Type a command to run before launching the game or leave it blank to disable"
          makeOption(title, "sidecommand_start")

        val closecmd =
          val title = "Type a command to run after launching the game or leave it blank to disable"
          makeOption(title, "sidecommand_close")

        if askSteamRun() then
          Vector(command, wine, startcmd, closecmd, "use_steam-run=true") ++ cfg
        else
          Vector(command, wine, startcmd, closecmd) ++ cfg
      else
        cfg
//     val overwrite = askPrompt("Would you like to overwrite the old configuration?")
    writeConfig(fullcfg, overwrite)
