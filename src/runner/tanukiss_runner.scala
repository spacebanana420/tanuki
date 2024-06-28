package tanuki.runner

import tanuki.{ffmpeg_path, ffplay_path, system_platform, recording_supported, ffmpeg_installed}
import tanuki.Platform
import tanuki.config.*
import bananatui.*
import ffscala.capture.*, ffscala.*

import scala.util.Sorting.quickSort
import java.io.File

private def generate_name(name: String, path: String, fmt: String, i: Int = -1): String =
  if i < 0 && !File(s"$path/$name.$fmt").isFile() then name
  else if i >= 0 && !File(s"$path/$name-$i.$fmt").isFile() then s"$name-$i"
  else generate_name(name, path, fmt, i+1)

def tanukiss_takeScreenshot() =
  if !recording_supported then pressToContinue("Screenshotting is not supported by MacOS yet!")
  else if !ffmpeg_installed then pressToContinue("FFmpeg is not installed in your system or configured in Tanuki!\nFFmpeg is required to capture screenshots")
  else
    val cfg = readConfig()
    val ss_path = get_screenshot_path(cfg)
    val ssdelay = get_screenshot_delay(cfg)
    val ssfmt = get_screenshot_format(cfg)
    val ss_name = generate_name("tanuki-screenshot", ss_path, ssfmt)
    val fullpath = s"$ss_path/$ss_name.$ssfmt"
    val ss_args =
      ssfmt match
        case "avif" =>
          setVideoEncoder("av1")
          av1_stillPicture()
          ++ av1_setDeadline("good")
          ++ av1_setcpu_used(6)
          ++ setCRF(0)
        case "jpg" =>
          val quality = get_screenshot_jpg_quality(cfg)
          setQuality(quality)
        case _ =>
          val pred = get_screenshot_png_pred(cfg)
          png_setPred(pred)
    val capture_mode = if system_platform == Platform.Windows then "gdigrab" else "x11grab"
    val input = if system_platform == Platform.Windows then "" else "0.0"

    if ssdelay > 0 then
      println(s"Taking a screenshot in $ssdelay milliseconds")
      Thread.sleep(ssdelay)
    println("Capturing and saving screenshot...")
    val status = takeScreenshot_auto(capture_mode, input, fullpath, args = ss_args)
    if status == 0 then pressToContinue(s"Screenshot successfully taken in $fullpath")
    else pressToContinue("Tanuki failed to take a screenshot!")

def tanukiss_viewScreenshots(title: String): Unit =
  def isImage(name: String): Boolean =
    if name.contains(".png") || name.contains(".avif") || name.contains(".jpg") then true
    else false

  val cfg = readConfig()
  val ss_path = get_screenshot_path(cfg)
  val screenshots =
    File(ss_path).list()
    .filter(x => File(s"$ss_path/$x").isFile() && isImage(x))
  if screenshots.length == 0 then pressToContinue(s"No screenshots have been found in $ss_path!")
  else
    quickSort(screenshots)
    val image = chooseOption_hs(screenshots.toVector, 2, s"$title\n\nChoose a screenshot\nYou can toggle fullscreen by pressing F in the image viewer and you can quit by pressing ESC or Q\n") //remove toVector later
    if image != "" then
      execplay(s"$ss_path/$image", setWindowTitle("Tanuki Screenshot"))
      tanukiss_viewScreenshots(title)
