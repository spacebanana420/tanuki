package tanuki.config

import java.io.File
import scala.io.Source

def isConfigOK(cfg: List[String], games: List[String], datas: List[String]): Boolean =
  if check_paths(games, true) && check_paths(datas, false) then
    true
  else
    false

private def check_paths(entries: List[String], isGame: Boolean, i: Int = 0): Boolean =
  def isPathCorrect(path: String): Boolean =
    if (isGame && File(path).isFile()) || (isGame == false && File(path).isDirectory()) then
      true
    else
      false

  if i >= entries.length then
    true
  else
    val entry = parseEntry(entries(i))
    if entry(0) != "" && entry(1) != "" && isPathCorrect(entry(1)) then
      check_paths(entries, isGame, i+1)
    else
      false
