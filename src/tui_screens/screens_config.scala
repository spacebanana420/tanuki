package tanuki.tui

import tanuki.system_platform
import bananatui.*
import tanuki.config.*
import tanuki.Platform
import java.io.File
import scala.sys.exit

def tui_noentries(entries: Seq[String]): Boolean =
  if entries.length == 0 then
    val text = "No entries have been found!\nWould you like to add some to your configuration?"
    val answer = askPrompt(text)
    if answer then
      tui_configure(false)
    true
  else
    false

private def addGame(): String =
    val name = readUserInput("Type the name of your game entry to add (for example: Touhou 10)")
    val path = readUserInput("Type the path to your game's executable")
    s"game=$name:$path"


private def addData(): String =
    val name = readUserInput("Type the name of your game entry to add (for example: Touhou 10 data)")
    val path = readUserInput("Type the path to the directory")
    s"data=$name:$path"

private def addGamecmd(): String =
    val name = readUserInput("Type the name of your command entry to add (for example: Open Github)")
    val path = readUserInput("Type the command (for example: firefox https://github.com)")
    s"game_cmd=$name:$path"

private def askSteamRun(): Boolean =
    if File("/nix/store").isDirectory() then
      val usesteamrun = askPrompt(s"It seems you are using NixOS\nIf you are running a custom third party wine build, it might not work out of the box\nWould you like to enable the use of steam-run to fix this?")
      if usesteamrun then
        true
      else
        false
    else
      false

private def configureSS(): String =
  val fmts = Vector("png", "jpg", "avif")
  val f = chooseOption_string(fmts, "Choose an image format", "Default (png)")
  val format = if f == "" then "png" else f
  val delay = readInt("Type the delay to take screenshots in milliseconds or type 0 to disable")
  val path = chooseOption_dir("Type the path to store your screenshots or leave it blank to use Tanuki's directory")

  val opt1 = s"screenshot_format=$format"
  val opt2 = if delay > 0 then s"\nscreenshot_delay=$delay" else ""
  val opt3 = s"\nscreenshot_path=$path"
  s"$opt1$opt2$opt3" //improve later

private def configureFFmpeg(): String =
  val ffmpeg_path = readUserInput("Type the path to FFmpeg or leave it blank to use the system FFmpeg")
  val ffplay_path = readUserInput("Type the path to FFplay or leave it blank to use the system FFplay")
  val arg_peg = if ffmpeg_path == "" then "" else s"ffmpeg_path=$ffmpeg_path"
  val arg_play = if ffplay_path == "" then "" else s"ffplay_path=$ffplay_path"
  if ffmpeg_path != "" && ffplay_path != "" then s"$arg_peg\n$arg_play" else s"$arg_peg$arg_play"

def tui_configure(overwrite: Boolean) =
  def makeOption(title: String, setting: String): String =
    val ans = readUserInput(title)
    if ans != "" then
      s"$setting=$ans"
    else
      ""

  def menu(cfg_settings: Vector[String] = Vector(), screenshot: String = "", ffmpeg: String = ""): Vector[String] =
    val default_opt = if cfg_settings.length == 0 && screenshot == "" && ffmpeg == "" then "Cancel" else "Done"
    val opts = Vector("Add game entry", "Add data entry", "Add command entry", "Configure Tanuki's screenshotter", "Configure FFmpeg/FFplay")

    val choice = chooseOption(opts, s"Choose what to configure", default_opt)
    choice match
      case 0 =>
        if screenshot == "" && ffmpeg == "" then cfg_settings
        else cfg_settings :+ screenshot :+ ffmpeg
      case 1 =>
        menu(cfg_settings :+ addGame(), screenshot, ffmpeg)
      case 2 =>
        menu(cfg_settings :+ addData(), screenshot, ffmpeg)
      case 3 =>
        menu(cfg_settings :+ addGamecmd(), screenshot, ffmpeg)
      case 4 =>
        menu(cfg_settings, configureSS(), ffmpeg)
      case 5 =>
        menu(cfg_settings, screenshot, configureFFmpeg())
      case _ =>
        menu(cfg_settings)

  val cfg = menu()
  if cfg.length != 0 then
    val fullcfg =
        val command =
          val title = "Type the command/program to launch your native games with or leave it blank to disable"
          makeOption(title, "runner")

        val wine =
          if system_platform != Platform.Windows then
            val title = "Type the command/path to the WINE helper or leave it blank to disable\nIf you have wine installed in your system, you can type \"wine\""
            makeOption(title, "wine")
          else ""

        val startcmd =
          val title = "Type a command to run before launching your games or leave it blank to disable"
          makeOption(title, "sidecommand_start")

        val closecmd =
          val title = "Type a command to run after launching your games or leave it blank to disable"
          makeOption(title, "sidecommand_close")

        val closegame =
          val title = "Would you like Tanuki to close your game when you return to the main screen?"
          if askPrompt(title) then "return_closegame=yes" else ""

        if askSteamRun() then
          Vector(command, wine, startcmd, closecmd, closegame, "use_steam-run=true") ++ cfg
        else
          Vector(command, wine, startcmd, closecmd, closegame) ++ cfg
//     val overwrite = askPrompt("Would you like to overwrite the old configuration?")
    if overwrite then
      writeConfig(fullcfg, false)
    else
      val cfg = readConfig()
      val keep_games = getGames(cfg).map(x => s"game=$x") :+ "\n"
      val keep_datas = getGames_cmd(cfg).map(x => s"game_cmd=$x") :+ "\n"
      val keep_cmds = getDatas(cfg).map(x => s"data=$x") :+ "\n"
      writeConfig(keep_games ++ keep_datas ++ keep_cmds ++ fullcfg, false)
    pressToContinue("Configuration finished!")
