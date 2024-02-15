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

private def getList(l: List[String], txt: String = s"Choose an entry\n\n${green}${0}:${default} Exit\n\n", i: Int = 0): String =
  if i >= l.length then
    txt
  else
    val line = s"${green}${i+1}:${default} ${l(i)}\n"
    getList(l, txt + line, i+1)

private def readLoop(txt: String, maxval: Int): Int =
  val answer = answerToNumber(spawnAndRead(txt))
  if answer == 0 || (1 to maxval).contains(answer) then
    answer
  else
    readLoop(txt, maxval)

private def readLoop_list(l: List[String], title: String = s"Choose an entry\n\n${green}${0}:${default} Exit\n\n"): Int =
  val txt_list = getList(l, title)
  readLoop(txt_list, l.length)

private def readLoop_int(txt: String): Int =
  val answer = answerToNumber(spawnAndRead(txt))
  if answer != -1 then
    answer
  else
    readLoop_int(txt)

private def readLoop_dir(txt: String): String =
  val answer = spawnAndRead(txt)
  if File(answer).isDirectory() then
    answer
  else if answer == "" then
    "."
  else
    pressToContinue("That is not a real path in your system!")
    readLoop_dir(txt)

def tui_title() =
  while true do
    val quote = getRandomQuote()
    val text = s"$yellow[Tanuki Launcher]$default version 0.4\n\n$quote\n\n${green}0:$default Exit\n${green}1:$default Play\n${green}2:$default Play and record\n${green}3:$default View screenshots\n${green}4:$default Compress screenshots\n${green}5:$default Configure launcher\n${green}6:$default Configure video recording\n"
    val answer = readLoop(text, 6)
    answer match
      case 0 =>
        exit()
      case 1 =>
        tui_play()
      case 2 =>
        tui_play(true)
      case 3 =>
        tui_ssview()
      case 4 =>
        tui_ssconv()
      case 5 =>
        val cfg = tui_configure()
        val overwrite = askPrompt("Would you like to overwrite the old configuration?")
        writeConfig(cfg, overwrite)
      case 6 =>
        tui_configureRecording()

def tui_noffmpeg(): Boolean =
  if !ffmpeg_installed then
    val text = s"FFmpeg wasn't found in your system!'\nFFmpeg is required for this functionality!"
    pressToContinue(text)
    true
  else
    false

def tui_play(record: Boolean = false) =
  if record && !tui_noffmpeg() && rec_configExists() then
    val reccfg = rec_readConfig()
    if !rec_isConfigOk(reccfg) then
      tui_recconfigerror()
    else
      tui_play_generic(true, reccfg)
  else if record then tui_recmissingconfig()
  else
    tui_play_generic()


private def tui_play_generic(record: Boolean = false, reccfg: List[String] = List()) =
  val games = getGames(readConfig())
  if !tui_noentries(games) then
    val names = games.map(x => parseEntry(x)(0))
    val paths = games.map(x => parseEntry(x)(1))

    val answer = readLoop_list(names, s"Choose a game to play\n\n${green}${0}:${default} Exit\n\n")
    if answer != 0 then
      if record then
        println(s"Launching ${names(answer-1)}\n\nGirls are now praying, please wait warmly...")
        launchGame(paths(answer-1), true, reccfg)
      else
        println(s"Launching ${names(answer-1)}\n\nGirls are now praying, please wait warmly...")
        launchGame(paths(answer-1))
// def tui_chooseScreenshot(datapath: String): String =
//   def file(path: String) =
//     val images = File(path)
//       .list()
//       .toList
//       .filter(x => File(s"$path/$x").isFile && (x.contains(".png") || x.contains(".bmp")))
//     val answer = readLoop_list(images, s"Choose a screenshot\n\n${green}${0}:${default} Exit\n\n")
//     if answer != 0 then
//       s"$path/${images(answer-1)}"
//     else
//       ""
//
//   val dirs = getScreenshotDirs(datapath)
//   val answer = readLoop_list(dirs, s"The following screenshot folders in $datapath were found\nChoose a screenshot folder\n\n${green}${0}:${default} Exit\n\n")
//   if answer != 0 then
//     file(s"$datapath/${dirs(answer-1)}")
//   else
//     ""

def tui_ssentry(manualdata: List[String] = List()): String =
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

def tui_ssimage(path: String): String =
  val dir = tui_ssdir(path)
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
  val datas = getDatas(readConfig())
  if !tui_noffmpeg() && !tui_noentries(datas) then
    val entry = tui_ssentry(datas)
    if entry != "" then
      val ssimage = tui_ssimage(entry)
      if ssimage != "" then screenshot_view(ssimage)

//incompatible with the other functions, not using for now
def tui_ssoptions(path: String, image: String) =
  val answer = readLoop_list(List("Convert", "Crop"), s"What would you like to do with this screenshot?\n\n${green}${0}:${default} Exit\n\n")
  if answer == 1 then
    println("a") //finish
    //tui_ssconv(path, image)
  else if answer == 2 then
    println("a") //temp
    //tui_sscrop(image)
    //finish

def tui_ssconv() =
  val datas = getDatas(readConfig())
  if !tui_noffmpeg() && !tui_noentries(datas) then
    val entry = tui_ssentry(datas)
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


// def tui_data() =
//   val data = getDatas(readConfig())
//   val names = data.map(x => parseEntry(x)(0))
//   val paths = data.map(x => parseEntry(x)(1))
//   val text = getList(names)
//
//   var done = false
//   while done == false do
//     val answer = answerToNumber(spawnAndRead(text))
//
//     if answer == 0 then
//       done = true
//     else if (1 to names.length).contains(answer) then
//       done = true
//       println(s"Launching ${names(answer-1)}\nGirls are now praying, please wait warmly...")
//       launchGame(paths(answer-1))
//
//
// def tui_opendata() =
