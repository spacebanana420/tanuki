package tanuki.config

import tanuki.misc.similarInList
import java.io.File
import java.io.FileOutputStream
import scala.io.Source

def getRunner(cfg: Seq[String]): String = getFirstValue(cfg, "runner=")
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

def get_screenshot_delay(cfg: Seq[String]): Int =
  val delay_str = getFirstValue(cfg, "screenshot_delay=")
  try delay_str.toInt
  catch case e: Exception => 0

def get_screenshot_format(cfg: Seq[String]): String =
  val fmt = getFirstValue(cfg, "screenshot_format=").toLowerCase()
  if fmt == "jpg" || fmt == "avif" then fmt else "png"

def get_screenshot_path(cfg: Seq[String]): String =
  val path = getFirstValue(cfg, "screenshot_path=")
  if path != "" && File(path).isDirectory() && File(path).canWrite() then path else "."
