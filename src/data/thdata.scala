package tanuki.data

import tanuki.misc.*, tanuki.tui.*


import java.io.File
import java.nio.file.StandardCopyOption.*;
import java.nio.file.Files
import java.nio.file.Path

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

def getTHData(path: String): List[String] =
  val raw = File(path).list().toList
  val dirs = raw.filter(x => isDirRelevant(x, path))
  val files = raw.filter(x => isFileRelevant(x, path))
  List("dirs") ::: dirs ::: List("files") ::: files

def backupScoreFile(path: String): Byte = //implement optional 7zip support, and a config setting for toggling use of 7zip
  val pathfile = File(path)
  val dir_path = s"$path/tanuki_scorebackup"

  if pathfile.isDirectory() && pathfile.canWrite() then
    val scorefiles =
      pathfile.list()
      .filter(x => File(s"$path/$x").isFile() && File(s"$path/$x").length() < 1000000 && x.contains("score") && x.contains(".dat")) //filter out files bigger than 1MB, touhou scorefiles are way smaller than that
    if !File(dir_path).isDirectory() then File(dir_path).mkdir() //maybe mkdir() already checks for existing directories?
    for f <- scorefiles do
      val source = Path.of(s"$path/$f")
      val target = Path.of(s"$dir_path/$f")
      Files.copy(source, target, REPLACE_EXISTING)
    if scorefiles.length == 0 then 1 else 2
  else 0

def tui_backupScore() =
  val datadir = tui_chooseDataDir()
  val result = backupScoreFile(datadir)
  result match
    case 0 => pressToContinue("The data entry does not lead to a directory, or the directory does not have write access!")
    case 1 => pressToContinue("No scorefile was found!")
    case _ => pressToContinue("Successfully backed up all found scorefiles in \"tanuki_scorebackup\"!")
