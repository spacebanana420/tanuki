package tanuki.tui

import tanuki.{ffmpeg_installed, ffplay_installed, system_platform, platformcheck}
import tanuki.Platform
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
    val title = s"$yellow[Tanuki Launcher]$default version 0.10\n\n${getRandomQuote()}"
    val options = Vector("Play", "Play and Record", "Run Command\n", "Record Video", "Take Screenshot", "Manage Screenshots\n", "Manage Touhou Data", "View Recorded Footage", "Configure Tanuki", "Show Runtime Info")
    chooseOption(options, title, "Quit Tanuki") match
      case 0 =>
        exit()
      case 1 =>
        tui_play(title)
      case 2 =>
        tui_play_record(title)
      case 3 =>
        launchCommand(title)
      case 4 =>
        if rec_isRecordingSupported() then
          recordGameplay(waitconfirm = false)
      case 5 =>
        tanukiss_takeScreenshot()
      case 6 =>
        tui_manageScreenshots(title)
      case 7 =>
        tui_manageData(title)
      case 8 =>
        if rec_configExists() then
          tui_movieMenu()
        else
          pressToContinue("The file video_config.txt was not found!\nYou need it to watch your recorded footage!")
      case 9 =>
        tui_configureTanuki(title)
      case 10 =>
        platformcheck.printSystemInfo(title)

def tui_manageData(title: String): Unit =
  val opts =
    if xdg_supported(system_platform) then
      Vector("View Screenshots", "Compress Screenshots", "Crop Screenshots\n", "Backup Scorefiles", "View Replays", "Open Data Folder")
    else
      Vector("View Screenshots", "Compress Screenshots", "Crop Screenshots\n", "Backup Scorefiles", "View Replays")

  val choice = chooseOption(opts, title, "Return")
  if choice != 0 then
    choice match
      case 1 => tui_ssview()
      case 2 => tui_ssconv()
      case 3 => tui_sscrop()
      case 4 => tui_backupScore()
      case 5 => listReplays()
      case 6 => tui_ss_openfolder()
    tui_manageData(title)

def tui_configureTanuki(title: String): Unit =
  val opts =
    if xdg_supported(system_platform) then
      Vector("Configure Tanuki", "Configure Video Recording", "Open Tanuki Configuration", "Open Video Configuration")
    else
      Vector("Configure Tanuki", "Configure Video Recording")
  val choice = chooseOption(opts, title, "Return")
  if choice != 0 then
    choice match
      case 1 => tui_configure(false)
      case 2 => tui_configureRecording()
      case 3 => xdg_open("config.txt")
      case 4 => xdg_open("video_config.txt")
    tui_configureTanuki(title)

def tui_manageScreenshots(title: String): Unit =
  val opts = Vector("View Screenshots", "Crop Screenshots")
  val choice = chooseOption(opts, title, "Return")
  if choice != 0 then
    choice match
      case 1 =>
        if !ffplay_installed then pressToContinue("You require FFplay to be installed to view your screenshots from Tanuki!")
        else tanukiss_viewScreenshots(title)
      case 2 =>
        if !ffplay_installed && !ffmpeg_installed then
          pressToContinue("You require FFmpeg and FFplay to be installed to crop your Tanuki screenshots!")
        else tanukiss_cropSreenshot()
    tui_manageScreenshots(title)

def tui_noffmpeg() =
  if !ffmpeg_installed then
    val text = 
      if system_platform == Platform.Windows then
        s"FFmpeg wasn't found in your system.\nFFmpeg is required for this functionality! If FFmpeg is not in your system's PATH, then you must specify the path to it in Tanuki's config."
      else
        s"FFmpeg wasn't found in your system.\nFFmpeg is required for this functionality!"
    pressToContinue(text)
    true
  else
    false

def tui_noffplay() =
  if !ffplay_installed then
    val text =
      if system_platform == Platform.FreeBSD then //freebsd stuff
        s"FFplay wasn't found in your system!\nFFmpeg is required for this functionality!\nOn FreeBSD, to make use of FFplay, FFmpeg must be build from source with SDL support enabled!"
      else
        s"FFplay wasn't found in your system!\nFFplay is required for this functionality! It comes with FFmpeg by default for most systems."
    pressToContinue(text)
    true
  else
    false

def tui_play_record(title: String) =
  val rec_cfg = rec_readConfig()
  if rec_isRecordingSupported(rec_cfg) then
    tui_play(title, true, rec_cfg)


def tui_play(title: String, record: Boolean = false, reccfg: Seq[String] = List()) =
  val games = getGames(readConfig())
  if !tui_noentries(games) then
    val names = games.map(x => parseEntry(x)(0))
    val paths = games.map(x => parseEntry(x)(1))

    val answer = chooseOption(names, s"$title\n\nChoose a game to play")
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
