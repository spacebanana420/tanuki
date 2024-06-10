package tanuki.tui

import tanuki.{ffmpeg_installed, ffplay_installed, system_platform, platformcheck}
import tanuki.runner.*
import tanuki.config.*
import tanuki.data.*
import tanuki.quotes.*
import tanuki.recorder.*
import tanuki.misc.{xdg_open, xdg_supported}

import bananatui.*
import ffscala.*
import java.io.File
import scala.sys.exit
import sys.process.*

val green = foreground("green")
val default = foreground("default")
val yellow = foreground("yellow")

def tui_title() =
  while true do
    val title = s"$yellow[Tanuki Launcher]$default version 0.8.1\n\n${getRandomQuote()}"
    val options = Vector("Play", "Play and record", "Record video", "Manage Touhou data", "View recorded footage", "Configure Tanuki", "Show runtime info")
//     val text = //revamp this with my new banantui instead
//       s"$title\n\n"
//       + s"${green}0:$default Exit"
//       + s"\n\n${green}1:$default Play\n${green}2:$default Play and record"
//       + s"\n${green}3:$default Record video"
//       + s"\n${green}4:$default Manage Touhou data"
//       + s"\n${green}5:$default View recorded footage"
//       + s"\n\n${green}6:$default Configure Tanuki"
//     val answer = readLoop(text, 9)
    chooseOption(options, title, "Quit Tanuki") match
      case 0 =>
        exit()
      case 1 =>
        tui_play()
      case 2 =>
        tui_play(true)
      case 3 => //finish this
        if rec_isRecordingSupported() then
          recordGameplay(waitconfirm = false)
      case 4 =>
        tui_manageData(title)
      case 5 =>
        if rec_configExists() then
          tui_movieMenu()
        else
          pressToContinue("The file video_config.txt was not found!\nYou need it to watch your recorded footage!")
      case 6 =>
        tui_configureTanuki(title)
      case 7 => platformcheck.printSystemInfo(title)

def tui_manageData(title: String): Unit =
  val opts =
    if xdg_supported(system_platform) then
      Vector("View screenshots", "Compress screenshots", "Backup scorefiles", "Open data folder")
    else
      Vector("View screenshots", "Compress screenshots", "Backup scorefiles")

  val choice = chooseOption(opts, title, "Return")
  if choice != 0 then
    choice match
      case 1 => tui_ssview()
      case 2 => tui_ssconv()
      case 3 => tui_backupScore()
      case 4 => tui_ss_openfolder()
    tui_manageData(title)

def tui_configureTanuki(title: String): Unit =
  val opts =
    if xdg_supported(system_platform) then
      Vector("Configure games and runners", "Configure video recording", "Open Tanuki configuration", "Open Video Configuration")
    else
      Vector("Configure games and runners", "Configure video recording")
  val choice = chooseOption(opts, title, "Return")
  if choice != 0 then
    choice match
      case 1 => tui_configure(true)
      case 2 => tui_configureRecording()
      case 3 => xdg_open("config.txt")
      case 4 => xdg_open("video_config.txt")
    tui_configureTanuki(title)

def tui_noffmpeg(): Boolean =
  if !ffmpeg_installed then
    val text = s"FFmpeg wasn't found in your system!\nFFmpeg is required for this functionality!"
    pressToContinue(text)
    true
  else
    false

def tui_noffplay(): Boolean =
  if !ffplay_installed then
    val text =
      if system_platform == 4 then //freebsd stuff
        s"FFplay wasn't found in your system!\nFFmpeg is required for this functionality!\nOn FreeBSD, to make use of FFplay, FFmpeg must be build from source with SDL support enabled!"
      else
        s"FFplay wasn't found in your system!\nFFplay is required for this functionality! It comes with FFmpeg by default for most systems."
    pressToContinue(text)
    true
  else
    false

def tui_play(record: Boolean = false) =
  def everythingOk(i: Int = 0): Boolean = //replace with rec_isRecordingSupported()
    if i >= 3 then
      true
    else
      val ok =
        i match
          case 0 => !tui_noffmpeg()
          case 1 => rec_configExists()
          case 2 => tui_supportedOS()

      if ok then everythingOk(i+1)
      else
        if i == 1 then tui_recmissingconfig()
        false

  if record && everythingOk() then
    val reccfg = rec_readConfig()
    if rec_isConfigOk(reccfg) then
      tui_play_generic(true, reccfg)
    else
      tui_recconfigerror()
  else if !record then
    tui_play_generic()


private def tui_play_generic(record: Boolean = false, reccfg: Seq[String] = List()) =
  val games = getGames(readConfig())
  if !tui_noentries(games) then
    val names = games.map(x => parseEntry(x)(0))
    val paths = games.map(x => parseEntry(x)(1))

    val answer = chooseOption(names, s"Choose a game to play")
    if answer != 0 then
      println(s"Launching ${names(answer-1)}\n\nGirls are now praying, please wait warmly...")

      if record then launchGame(paths(answer-1), names(answer-1), true, reccfg)
      else launchGame(paths(answer-1), names(answer-1))

def tui_chooseDataDir(manualdata: List[String] = List()): String =
  val datas =
    if manualdata == List() then
      getDatas(readConfig())
    else
      manualdata
  val names = datas.map(x => parseEntry(x)(0))
  val paths = datas.map(x => parseEntry(x)(1))

  val answer = chooseOption(names)
  if answer != 0 then
    paths(answer-1)
  else
    ""

def tui_ssdir(path: String): String =
  val dirs = getScreenshotDirs(path)
  val answer = chooseOption(dirs, s"The following screenshot folders in $path were found\nChoose a screenshot folder")
  if answer != 0 then
    s"$path/${dirs(answer-1)}"
  else
    ""

def tui_ssimage(dir: String): String =
  if dir != "" then
    val images = listScreenshots(dir)
    val answer = chooseOption_h(images, 3, "Choose a screenshot")
    if answer != 0 then
      s"$dir/${images(answer-1)}"
    else
      ""
  else
    ""

def tui_ssview() =
  def viewLoop(dir: String): Unit =
    val ssimage = tui_ssimage(dir)
    if ssimage != "" then
      screenshot_view(ssimage)
      viewLoop(dir)
  
  val datas = getDatas(readConfig())
  if !tui_noffplay() && !tui_noentries(datas) then
    val entry = tui_chooseDataDir(datas)
    if entry != "" then
      val dir = tui_ssdir(entry)
      viewLoop(dir)

def tui_ssconv() =
  val datas = getDatas(readConfig())
  if !tui_noffmpeg() && !tui_noentries(datas) then
    val entry = tui_chooseDataDir(datas)
    if entry != "" then
      val ssdir = tui_ssdir(entry)
      if ssdir != "" then
        println("Converting all BMP screenshots into PNG copies")
        val pngdir = File(s"$ssdir/PNG")
        if !pngdir.isDirectory() then pngdir.mkdir()

        val imgs = listScreenshots(ssdir, false)
        for x <- imgs do
          println(s"Compressing image \"$x\"")
          screenshot_convert(x, ssdir)
        pressToContinue("All screenshots have been converted!\nTheir copies have been moved into a directory named \"PNG\"!")

def tui_ss_openfolder() =
  val datas = getDatas(readConfig())
  val entry = tui_chooseDataDir(datas)
  if entry != "" then xdg_open(entry)
