package tanuki.tui

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
    val cfg = tui_configure()
    writeConfig(cfg, false)

def tui_noentries(entries: Seq[String]): Boolean =
  if entries.length == 0 then
    val text = "No entries have been found!\nWould you like to add some to your configuration?"
    val answer = askPrompt(text)
    if answer then
      val cfg = tui_configure(false)
      writeConfig(cfg, true)
    true
  else
    false

def tui_configure(fullconfig: Boolean = true): Vector[String] =
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

  def menu(l: Vector[String] = Vector()): Vector[String] =
    val choice = chooseOption(Vector("Game", "Data"),s"Choose the entry type to add", "Done")
    choice match
      case 0 =>
        l
      case 1 =>
        menu(l :+ addGame())
      case 2 =>
        menu(l :+ addData())
      case _ =>
        menu(l)

  val cfg = menu()
  if fullconfig then
    val command =
      val ans = readUserInput(s"Type the command/program to launch Touhou with or leave it blank to disable")
      if ans != "" then
        s"command=$ans"
      else
        ""
    val startcmd =
      val ans = readUserInput(s"Type the command to run before launching Touhou or leave it blank to disable")
      if ans != "" then
        s"sidecommand_start=$ans"
      else
        ""
    val closecmd =
      val ans = readUserInput(s"Type the command to run after closing Touhou or leave it blank to disable")
      if ans != "" then
        s"sidecommand_close=$ans"
      else
        ""

    if askSteamRun() then
      Vector(command, startcmd, closecmd, "use_steam-run=true") ++ cfg
    else
      Vector(command, startcmd, closecmd) ++ cfg
  else
    cfg
