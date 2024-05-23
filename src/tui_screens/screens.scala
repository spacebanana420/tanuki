package tanuki.tui

import tanuki.ffmpeg_installed
import tanuki.runner.*
import tanuki.config.*
import tanuki.data.*
import tanuki.quotes.*
import tanuki.recorder.*

import ffscala.*
import java.io.File
import scala.sys.exit

val green = foreground("green")
val default = foreground("default")
val yellow = foreground("yellow")

def tui_title() =
  while true do
    val quote = getRandomQuote()
    val text = //revamp this with my new banantui instead
      s"$yellow[Tanuki Launcher]$default version 0.7.1\n\n$quote\n\n"
      + s"${green}0:$default Exit"
      + s"\n\n${green}1:$default Play\n${green}2:$default Play and record"
      + s"\n${green}3:$default Record video only"
      + s"\n\n${green}4:$default View screenshots\n${green}5:$default Compress screenshots"
      + s"\n\n${green}6:$default Configure launcher\n${green}7:$default Configure video recording"
      + s"\n${green}8:$default View recorded footage\n"
      + s"\n${green}9:$default Backup scorefiles\n"
    val answer = readLoop(text, 9)
    answer match
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
        tui_ssview()
      case 5 =>
        tui_ssconv()
      case 6 =>
        val cfg = tui_configure()
        val overwrite = askPrompt("Would you like to overwrite the old configuration?")
        writeConfig(cfg, overwrite)
      case 7 =>
        tui_configureRecording()
      case 8 =>
        if rec_configExists() then
          tui_movieMenu()
        else
          pressToContinue("The file video_config.txt was not found!\nYou need it to watch your recorded footage!")
      case 9 =>
        tui_backupScore()


def tui_noffmpeg(): Boolean =
  if !ffmpeg_installed then
    val text = s"FFmpeg wasn't found in your system!\nFFmpeg is required for this functionality!"
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

    val answer = readLoop_list(names, s"Choose a game to play\n\n${green}${0}:${default} Exit\n\n")
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

  val answer = readLoop_list(names)
  if answer != 0 then
    paths(answer-1)
  else
    ""

def tui_ssdir(path: String): String =
  val dirs = getScreenshotDirs(path)
  val answer = readLoop_list(dirs, s"The following screenshot folders in $path were found\nChoose a screenshot folder\n\n${green}${0}:${default} Exit\n\n")
  if answer != 0 then
    s"$path/${dirs(answer-1)}"
  else
    ""

def tui_ssimage(dir: String): String =
  if dir != "" then
    val images = listScreenshots(dir)
    val answer = readLoop_list(images, s"Choose a screenshot\n\n${green}${0}:${default} Exit\n\n")
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
  if !tui_noffmpeg() && !tui_noentries(datas) then
    val entry = tui_chooseDataDir(datas)
    if entry != "" then
      val dir = tui_ssdir(entry)
      viewLoop(dir)

//incompatible with the other functions, not using for now
// def tui_ssoptions(path: String, image: String) =
//   val answer = readLoop_list(List("Convert", "Crop"), s"What would you like to do with this screenshot?\n\n${green}${0}:${default} Exit\n\n")
//   if answer == 1 then
//     println("a") //finish
//     //tui_ssconv(path, image)
//   else if answer == 2 then
//     println("a") //temp
//     //tui_sscrop(image)
//     //finish

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

//def tui_sscrop(image: String) =
