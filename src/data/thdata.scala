package tanuki.data

import tanuki.misc.*, tanuki.tui.*


import java.io.File
import java.nio.file.StandardCopyOption.REPLACE_EXISTING;
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

def backupScoreFile(path: String, name: String): Byte = //implement optional 7zip support, and a config setting for toggling use of 7zip
  val pathfile = File(path)
  val dir_path = s"$path/$name"

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

private def generateDirName(path: String, name: String = "tanuki_scorebackup", i: Int = 0): String =
  val newname = if i == 0 then name else s"$name-$i"
  if !File(s"$path/$newname").isDirectory() then newname
  else generateDirName(path, name, i+1)

def tui_backupScore() =
  val green = foreground("green"); val default = foreground("default")
  val datadir = tui_chooseDataDir()
  if datadir != "" then
    val name =
      if askPrompt("Do you want to overwrite the current backup, if it exists?", false) then
        "tanuki_scorebackup"
      else generateDirName(datadir)

    val result = backupScoreFile(datadir, name)
    result match
      case 0 => pressToContinue(s"The data entry\n$green$datadir$default\ndoes not lead to a directory, or the directory does not have write access!")
      case 1 => pressToContinue(s"No scorefile was found!\nDirectory: $green$datadir$default")
      case _ => pressToContinue(s"Successfully backed up all scorefiles into $green\"$name\"$default!")
