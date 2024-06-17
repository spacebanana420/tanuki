package tanuki.data

import tanuki.misc.*
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
    case "14-18" =>
      Vector(69, 277, 755, 338)
    case "19" => Vector()

//def guessThGame(): List[Int] =
