package tanuki.runner

import tanuki.{ffmpeg_path, ffplay_path, system_platform, recording_supported, ffmpeg_installed}
import tanuki.config.*
import bananatui.pressToContinue
import ffscala.capture.*, ffscala.*

import java.io.File
import scala.sys.process.*

//temporary solution
def screenshot_view(path: String) =
  val cmdexec = Seq(ffplay_path, "-loglevel", "quiet", path)
  val parentpath = File(path).getParent()
  Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
  //execplay(path)

private def changeExtension(name: String, i: Int, s: String = "", copy: Boolean = false): String =
  def reverse(s: String, i: Int, ns: String = ""): String =
    if i < 0 then
      ns
    else
      reverse(s, i-1, ns + s(i))

  if i < 0 then
    reverse(s, s.length-1) + ".png"
  else if name(i) == '.' then
    changeExtension(name, i-1, s, true)
  else if copy then
    changeExtension(name, i-1, s + name(i), copy)
  else
    changeExtension(name, i-1, s, copy)

def screenshot_convert(name: String, path: String) =
  val newname = changeExtension(name, name.length-1)
  encode(s"$path/$name", s"$path/PNG/$newname", exec=ffmpeg_path)

def screenshot_crop(name: String, path: String, x: Int, y: Int, w: Int, h: Int) =
  if !File(s"$path/crop").isDirectory() then
    File(s"$path/crop").mkdir()
  val fcrop = crop(x, y, w, h)
  val newname = changeExtension(name, name.length-1)
  encode(s"$path/$name", s"$path/crop/$newname", filters=fcrop, exec=ffmpeg_path)


//the functions below are not related to touhou screenshots

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
          av1_stillPicture()
          ++ av1_setDeadline("good")
          ++ av1_setcpu_used(6)
          ++ setCRF(0)
        case "jpg" =>
          setQuality(1)
        case _ =>
          png_setPred("mixed")
    val capture_mode = if system_platform == 0 then "gdigrab" else "x11grab"
    val input = if system_platform == 0 then "" else "0.0"

    val status = takeScreenshot(capture_mode, input, fullpath, args = ss_args)
    if status == 0 then pressToContinue(s"Screenshot successfully taken in $fullpath")
    else pressToContinue("Tanuki failed to take a screenshot!")
