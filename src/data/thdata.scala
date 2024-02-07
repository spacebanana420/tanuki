package tanuki.data

import tanuki.misc.*
import java.io.File


def getTHData(path: String): List[String] =
  val raw = File(path).list().toList
  val dirs = raw.filter(x => isDirRelevant(x, path))
  val files = raw.filter(x => isFileRelevant(x, path))
  List("dirs") ::: dirs ::: List("files") ::: files


def getScreenshotDirs(path: String): List[String] =
  File(path)
    .list()
    .toList
    .filter(x => File(s"$path/$x").isDirectory() && isScreenshotDir(s"$path/$x"))

private def isScreenshotDir(path: String): Boolean =
  def hasImageFiles(l: Array[String], i: Int = 0): Boolean =
    if i >= l.length then
      false
    else if l(i).contains(".png") || l(i).contains(".bmp") then
      true
    else
      hasImageFiles(l, i+1)

  hasImageFiles(File(path).list())


private def isDirRelevant(name: String, path: String): Boolean =
  if File(s"$path/$name").isDirectory() && (name == "replay" || name == "snapshot") then
    true
  else
    false

private def isFileRelevant(name: String, path: String): Boolean =
  val names = List("th", "score", "bgm")
  val fmts = List(".dat", ".cfg")
  if File(s"$path/$name").isFile() && similarInList(name, names) && similarInList(name, fmts) then
    true
  else
    false
