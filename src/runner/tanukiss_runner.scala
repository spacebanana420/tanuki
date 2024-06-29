package tanuki.runner

import tanuki.{ffmpeg_path, ffplay_path, ffprobe_path, system_platform, recording_supported, ffmpeg_installed}
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

private def isImage(name: String): Boolean =
  if name.contains(".png") || name.contains(".avif") || name.contains(".jpg") || name.contains(".bmp") then true
  else false

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

private def browseScreenshots(dir: String, title: String): String =
  val screenshots =
      File(dir).list()
      .filter(x => File(s"$dir/$x").isFile() && isImage(x))
  if screenshots.length == 0 then
    pressToContinue(s"No screenshots have been found in $dir!")
    ""
  else
    quickSort(screenshots)
    chooseOption_hs(screenshots.toVector, 2, title) //remove toVector later

def tanukiss_viewScreenshots(title: String): Unit =
  val cfg = readConfig()
  val ss_path = get_screenshot_path(cfg)
  val image = browseScreenshots(ss_path, s"$title\n\nChoose a screenshot\nYou can toggle fullscreen by pressing F in the image viewer and you can quit by pressing ESC or Q\n")
  if image != "" then
    execplay(s"$ss_path/$image", setWindowTitle("Tanuki Screenshot"))
    tanukiss_viewScreenshots(title)

private def generic_cropSreenshot(image_path: String, new_image_path: String) = //ffplay should start and close automatically, do that later
  val green = foreground("green"); val deflt = foreground("default")
  def setupCrop(x: Int, y: Int, w: Int, h: Int, resolution: List[Int]): List[String] =
    val title = s"Image's original resolution: $green${resolution(0)}x${resolution(1)}$deflt\nCurrent crop settings:\nx: $green$x$deflt\ny: $green$y$deflt\n\nwidth: $green$w$deflt\nheight: $green$h$deflt"
    val opts = Vector("Preview current configuration", "Change horizontal position", "Change vertical position", "Set width", "Set height")
    val answer = chooseOption(opts, title, "Done") 
    answer match
      case 0 => crop(x, y, w, h)
      case 1 =>
        execplay(image_path, filters = crop(x, y, w, h), exec=ffplay_path)
        setupCrop(x, y, w, h, resolution)
      case 2 =>
        val title = "Type the numerical value for the starting horizontal point"
        val answer = readInt(title)
        val new_x = if answer > resolution(0) then resolution(0) else answer
        setupCrop(new_x, y, w, h, resolution)
      case 3 =>
        val title = "Type the numerical value for the starting horizontal point"
        val answer = readInt(title)
        val new_y = if answer > resolution(1) then resolution(1) else answer
        setupCrop(x, new_y, w, h, resolution)
      case 4 =>
        val title = "Type the numerical value for the crop width"
        val answer = readInt(title)
        val new_width = if answer > resolution(0) then resolution(0) else answer
        setupCrop(x, y, new_width, h, resolution)
      case 5 =>
        val title = "Type the numerical value for the crop height"
        val answer = readInt(title)
        val new_height = if answer > resolution(1) then resolution(1) else answer
        setupCrop(x, y, w, new_height, resolution)
        
  val resolution = getResolution(image_path, exec=ffprobe_path) //remember to add ffprobe exec
  val crop_args = setupCrop(0, 0, 0, 0, resolution)
  encode(image_path, new_image_path, args=png_setPred("mixed"), filters=crop_args, exec=ffmpeg_path)
        
def tanukiss_cropSreenshot() =
  val cfg = readConfig()
  val screenshot_dir = get_screenshot_path(cfg)
  val img = browseScreenshots(screenshot_dir, "Choose a screenshot to manually crop")
  if img != "" then
    if !File(s"$screenshot_dir/crop").isDirectory() then File(s"$screenshot_dir/crop").mkdir()
    val newname = generate_name("tanuki-screenshot-crop", s"$screenshot_dir/crop/", "png")
    generic_cropSreenshot(s"$screenshot_dir/$img", s"$screenshot_dir/crop/$newname.png")
