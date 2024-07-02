package tanuki.config

import tanuki.misc.similarInList
import java.io.File
import java.io.FileOutputStream
import scala.io.Source


def configExists(): Boolean = File("config.txt").exists()

def readConfig(): Vector[String] =
  val settings =
    Vector(
    "game=", "game_cmd=", "data=", "wine=", "runner=", "use_steam-run=",
    "sidecommand_start=", "sidecommand_close=",
    "ffmpeg_path=", "ffplay_path=", "ffprobe_path",
    "dxvk_framerate=", "wine_prefix=", "return_closegame=",
    "screenshot_delay=", "screenshot_path=", "screenshot_format=", //"screenshot_reducedview=",
    "screenshot_jpg_quality=", "screenshot_png_quality=", "screenshot_manual_delay="
    )
  val src = Source.fromFile("config.txt")
  val cfg = src
    .getLines()
    .filter(x => x.length > 0 && similarInList(x, settings) && x(0) != '#')
    .toVector
  src.close()
  cfg

private def getValue(l: String, setting: String, tmp: String = "", value: String = "", i: Int = 0): String =
  if i >= l.length || (i >= setting.length && setting != tmp) then
    value
  else if tmp == setting then
    getValue(l, setting, tmp, value + l(i), i+1)
  else
    getValue(l, setting, tmp + l(i), value, i+1)

private def getValues(cfg: Seq[String], setting: String, vals: List[String] = List(), i: Int = 0): List[String] =
  if i >= cfg.length then
    vals
  else
    val value = getValue(cfg(i), setting)
    if value != "" then
      getValues(cfg, setting, vals :+ value, i+1)
    else
      getValues(cfg, setting, vals, i+1)

private def getFirstValue(cfg: Seq[String], setting: String, i: Int = 0): String =
  if i >= cfg.length then
    ""
  else
    val value = getValue(cfg(i), setting)
    if value != "" then
      value
    else
      getFirstValue(cfg, setting, i+1)

def parseCommand(cmd: String, arg: String = "", cmdl: List[String] = List(), i: Int = 0): List[String] =
  if i >= cmd.length then
    if arg == "" then cmdl else cmdl :+ arg
  else if cmd(i) == ' ' then
    parseCommand(cmd, "", cmdl :+ arg, i+1)
  else
    parseCommand(cmd, arg + cmd(i), cmdl, i+1)

def parseEntry(entry: String, e1: String = "", e2: String = "", i: Int = 0, first: Boolean = true): List[String] =
  if i >= entry.length then
    List(e1, e2)
  else if entry(i) == ':' && first then
    parseEntry(entry, e1, e2, i+1, false)
  else if first then
    parseEntry(entry, e1 + entry(i), e2, i+1, first)
  else
    parseEntry(entry, e1, e2 + entry(i), i+1, first)
