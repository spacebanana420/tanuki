package tanuki.data

import tanuki.misc.*
import java.io.File


def getTHData(path: String): List[String] =
  val raw = File(path).list().toList
  val dirs = raw.filter(x => isDirRelevant(x, path))
  val files = raw.filter(x => isFileRelevant(x, path))
  List("dirs") ::: dirs ::: List("files") ::: files


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
