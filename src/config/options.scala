package tanuki.config

import tanuki.misc.similarInList
import java.io.File
import java.io.FileOutputStream
import scala.io.Source

def getGames(cfg: Seq[String]): List[String] = getValues(cfg, "game=")
//def getNativeGames(cfg: Seq[String]): List[String] = getValues(cfg, "native-game=")
def getDatas(cfg: Seq[String]): List[String] = getValues(cfg, "data=")

def getCommand(cfg: Seq[String]): String = getFirstValue(cfg, "command=")
def getWinePath(cfg: Seq[String]): String = getFirstValue(cfg, "wine=")
def getFFmpegPath(cfg: Seq[String]): String = getFirstValue(cfg, "ffmpeg_path=")

def getWinePrefix(cfg: Seq[String]): String =
  val prefix = getFirstValue(cfg, "wine_prefix=")
  if File(prefix).isDirectory() then prefix else ""

def getDxvkFramerate(cfg: Seq[String]): String = getFirstValue(cfg, "dxvk_framerate=")
def getReturnClose(cfg: Seq[String]): Boolean =
  val toggle_str = getFirstValue(cfg, "return_closegame=").toLowerCase()
  if toggle_str == "yes" || toggle_str == "true" then true else false

def steamRunEnabled(cfg: Seq[String]): Boolean =
  val steamrun = getFirstValue(cfg, "use_steam-run=").toLowerCase()
  steamrun == "true" || steamrun == "yes"

def getStartCmd(cfg: Seq[String]): List[String] = parseCommand(getFirstValue(cfg, "sidecommand_start="))
def getCloseCmd(cfg: Seq[String]): List[String] = parseCommand(getFirstValue(cfg, "sidecommand_close="))
