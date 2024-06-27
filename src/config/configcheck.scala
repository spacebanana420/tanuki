package tanuki.config

import tanuki.tui.tui_configure
import bananatui.*
import java.io.File
import scala.io.Source

private def reportError() =
  val text = "There's an error in your config.txt! Check the error messages above to see what's wrong.\n\nWould you like to configure Tanuki now and delete the current configuration file?"
  val answer =
  if askPrompt(text, false) then
    tui_configure(true)

def isConfigOk(report: Boolean = true): Boolean =
  val cfg = readConfig()
  val games = getGames(cfg)
  val datas = getDatas(cfg)
  val is_ok = check_paths(games, true) && check_paths(datas, false)
  if report && !is_ok then reportError()
  is_ok

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
        printStatus(s"Entry ${entries(i)} of type $etype\n\tConfiguration error: missing name\n")
        false
    val check_path =
      if entry(1) != "" && isPathCorrect(entry(1)) then
        true
      else
        if isGame then
          printStatus(s"Entry ${entries(i)} of type $etype\n\tConfiguration error: path does not lead to a file\nPath: ${entry(1)}\n")
        else
          printStatus(s"Entry ${entries(i)} of type $etype\n\tConfiguration error: path does not lead to a directory\nPath: ${entry(1)}\n")
        false

    if check_name && check_path then
      check_paths(entries, isGame, i+1)
    else
      false
