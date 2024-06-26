package tanuki.config

import bananatui.*
import java.io.File
import scala.io.Source

def isConfigOk(): Boolean =
  val cfg = readConfig()
  val games = getGames(cfg)
  val datas = getDatas(cfg)
  check_paths(games, true) && check_paths(datas, false)

private def check_paths(entries: List[String], isGame: Boolean, i: Int = 0): Boolean =
  def isPathCorrect(path: String): Boolean =
    if (isGame && File(path).isFile()) || (isGame == false && File(path).isDirectory()) then
      true
    else
      false

  val etype = if isGame then "game" else "data"
  if i >= entries.length then
    true
  else
    val entry = parseEntry(entries(i))
    val check_name =
      if entry(0) != "" then
        true
      else
        printStatus(s"You have a $etype entry that is missing a name!\n")
        false
    val check_path =
      if entry(1) != "" && isPathCorrect(entry(1)) then
        true
      else
        printStatus(s"You have a $etype entry has an incorrect path assigned!\n")
        false

    if check_name && check_path then
      check_paths(entries, isGame, i+1)
    else
      false
