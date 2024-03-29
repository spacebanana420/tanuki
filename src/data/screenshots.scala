package tanuki.data

import tanuki.misc.*
import java.io.File

def getScreenshotDirs(path: String): List[String] =
  File(path)
    .list()
    .filter(x => File(s"$path/$x").isDirectory() && isScreenshotDir(s"$path/$x"))
    .toList

def listScreenshots(path: String, include_png: Boolean = true): List[String] =
  File(path)
    .list()
    .filter(x => isScreenshot(x, path, include_png))
    .toList

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
def ssTemplate(game: String): List[Int] =
  game match
    case "6-8" =>
      List(36, 132, 377, 304)
    case "9" =>
      List(49, 158, 545, 275)
    case "10-12" =>
      List(33, 150, 380, 271)
    case "13" => List()
    case "modernbubble" => List()
    case "highres1" => List()
    case "highres2" => List()

//def guessThGame(): List[Int] =
