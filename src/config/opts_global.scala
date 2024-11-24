package tanuki.config

import tanuki.misc.similarInList
import java.io.File
import java.io.FileOutputStream
import scala.io.Source

def getRunner(cfg: Seq[String]): String = getFirstValue(cfg, "runner=")

def getWinePath(cfg: Seq[String]): String =
  val path = getFirstValue(cfg, "wine=")
  if path != "" then path else "wine"

def getFFmpegPath(cfg: Seq[String]): String = getFirstValue(cfg, "ffmpeg_path=")
def getFFplayPath(cfg: Seq[String]): String = getFirstValue(cfg, "ffplay_path=")
def getFFprobePath(cfg: Seq[String]): String = getFirstValue(cfg, "ffprobe_path=")

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

def getCompatLayer(cfg: Seq[String]): String = getFirstvalue(cfg, "compat_layer=")

def getStartCmd(cfg: Seq[String]): List[String] = parseCommand(getFirstValue(cfg, "sidecommand_start="))
def getCloseCmd(cfg: Seq[String]): List[String] = parseCommand(getFirstValue(cfg, "sidecommand_close="))

def get_screenshot_delay(cfg: Seq[String]): Int =
  val delay_str = getFirstValue(cfg, "screenshot_delay=")
  try
    val num = delay_str.toInt
    if num > 0 then num else 0
  catch case e: Exception => 0

def get_screenshot_manual_delay(cfg: Seq[String]): Boolean =
  val fmt = getFirstValue(cfg, "screenshot_manual_delay=").toLowerCase()
  fmt == "yes" || fmt == "true"

def get_screenshot_format(cfg: Seq[String]): String =
  val fmt = getFirstValue(cfg, "screenshot_format=").toLowerCase()
  if fmt == "jpg" || fmt == "avif" then fmt else "png"

def get_screenshot_path(cfg: Seq[String]): String =
  val path = getFirstValue(cfg, "screenshot_path=")
  if path != "" && File(path).isDirectory() && File(path).canWrite() then path else "."

// def get_screenshot_reducedview(cfg: Seq[String]): Boolean =
//   val setting = getFirstValue(cfg, "screenshot_reducedview=").toLowerCase()
//   setting == "yes" || setting == "true"

def get_screenshot_jpg_quality(cfg: Seq[String]): Short =
  try
    val q = getFirstValue(cfg, "screenshot_jpg_quality=").toShort
    if q >= 1 && q <= 20 then q else 1
  catch case e: Exception => 1

def get_screenshot_png_quality(cfg: Seq[String]): String =
  val q = getFirstValue(cfg, "screenshot_png_quality=").toLowerCase()
  if q != "high" && q != "medium" && q != "low" then "high"
  else q

def get_screenshot_png_pred(cfg: Seq[String]): String =
  val quality = get_screenshot_png_quality(cfg)
  quality match
    case "low" => "sub"
    case "medium" => "avg"
    case _ => "mixed"

def thss_skip_duplicates(cfg: Seq[String]): Boolean =
  val skip = getFirstValue(cfg, "thss_skip_duplicates=").toLowerCase()
  skip == "yes" || skip == "true"
