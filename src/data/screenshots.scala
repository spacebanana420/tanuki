package tanuki.data

import tanuki.misc.*
import tanuki.config.*
import tanuki.tui.*
import tanuki.runner.{screenshot_view, screenshot_convert, screenshot_crop}
import bananatui.*
import java.io.File

def getScreenshotDirs(path: String): Vector[String] =
  File(path)
    .list()
    .filter(x => File(s"$path/$x").isDirectory() && isScreenshotDir(s"$path/$x"))
    .toVector //remember to replace this

def listScreenshots(path: String, include_png: Boolean = true): Vector[String] =
  File(path)
    .list()
    .filter(x => isScreenshot(x, path, include_png))
    .toVector //remember to replace this

private def isScreenshot(name: String, path: String, include_png: Boolean = true): Boolean =
  if include_png then
    File(s"$path/$name").isFile() && (name.contains(".png") || name.contains(".bmp"))
  else
    File(s"$path/$name").isFile() && name.contains(".bmp")


private def isScreenshotDir(path: String): Boolean =
  def hasImageFiles(l: Array[String], i: Int = 0): Boolean =
    if i >= l.length then
      false
    else if isScreenshot(l(i), path) then
      true
    else
      hasImageFiles(l, i+1)

  hasImageFiles(File(path).list())

//unfinished and unused for now
def ssTemplate(game: String): Vector[Int] =
  game match
    case "6-8" =>
      Vector(36, 132, 377, 304)
    case "9" =>
      Vector(49, 158, 545, 275)
    case "10-12" =>
      Vector(33, 150, 380, 271)
    case "13" =>
      Vector(21, 173, 409, 181)
    case "14" =>
      Vector(69, 277, 755, 338)
    case "15-18" =>
      Vector(78, 330, 878, 365)
    case "19" => Vector()

def tui_ssdir(path: String): String =
  val dirs = getScreenshotDirs(path)
  dirs.length match
    case 0 =>
      pressToContinue(s"No snapshot directories were found in $path!")
      ""
    case 1 => s"$path/${dirs(0)}"
    case _ =>
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

def tui_sscrop() =
  def sscrop(dir: String, template: String) =
    val crop_params = ssTemplate(template)
    val imgs = listScreenshots(dir, true)
    for x <- imgs do
      println(s"Cropping image \"$x\"")
      screenshot_crop(x, dir, crop_params(0), crop_params(1), crop_params(2), crop_params(3))

  val datas = getDatas(readConfig())
  if !tui_noffmpeg() && !tui_noentries(datas) then
    val entry = tui_chooseDataDir(datas)
    if entry != "" then
      val ssdir = tui_ssdir(entry)
      if ssdir != "" then
        val title = "Choose a crop template based on the Touhou game\n\nTanuki will crop the screenshot to focus on the dialog\nDifferent Touhou games have different positionings and scalings of portraits and dialog boxes/bubbles\nThis feature is still experimental and not tested on all Touhou games"
        val template = chooseOption_string(Vector("6-8", "9", "10-12", "13", "14", "15-18"), title, "Cancel")
        if template != "" then
          println("Cropping all screenshots")
          sscrop(ssdir, template)
          pressToContinue("All screenshots have been cropped!\nTheir copies have been moved into a directory named \"crop\"!")

def tui_ss_openfolder() =
  val datas = getDatas(readConfig())
  val entry = tui_chooseDataDir(datas)
  if entry != "" then xdg_open(entry)

//def guessThGame(): List[Int] =
