package tanuki

import tanuki.tui.*
import tanuki.config.*
import bananatui.{foreground, pressToContinue, clear, windows_enableANSI}
import ffscala.checkFFmpeg
import java.io.File
import scala.sys.process.*

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

val ffmpeg_path = platformcheck.getFFmpeg(true)
val ffplay_path = platformcheck.getFFmpeg(false)
val ffprobe_path = platformcheck.getFFprobe()
var wine_installed = false
var ffmpeg_installed = false
var ffplay_installed = false 
var ffprobe_installed = false //handled by platformcheck

val system_platform = platformcheck.getPlatform()
val recording_supported = system_platform != Platform.MacOS

@main def main() =
  if !configExists() then createConfig()
  platformcheck.check()
  if isConfigOk() then tui_title()

enum Platform:
  case Windows, NixOS, Linux, MacOS, FreeBSD, Unknown
  //0,1,2,3,4, 5

object platformcheck:
  def getPlatform(): Platform =
    val platform = System.getProperty("os.name")
    if platform.contains("Windows") then Platform.Windows
    else if platform.contains("Linux") then if File("/nix/store").isDirectory() then Platform.NixOS else Platform.Linux
    else if platform.contains("Mac") then Platform.MacOS
    else if platform.contains("FreeBSD") then Platform.FreeBSD
    else Platform.Unknown

  def getFFmpeg(peg: Boolean): String =
    val cmd = if peg then "ffmpeg" else "ffplay"
    if configExists() then
      val exec = if peg then getFFmpegPath(readConfig()) else getFFplayPath(readConfig())
      if exec != "" && File(exec).isFile() then exec else cmd
    else cmd
    
  def getFFprobe(): String =
  if configExists() then
    val exec = getFFprobePath(readConfig())
    if exec != "" && File(exec).isFile() then exec else "ffprobe"
  else "ffprobe"

  def checkWINE(): Boolean =
    val config = readConfig()
    val wine_path = getWinePath(config)
    val cmd =
      if system_platform == Platform.NixOS && steamRunEnabled(config) then
        Vector("steam-run", wine_path, "--help")
      else
        Vector(wine_path, "--help")
    try
      val process = cmd.run(ProcessLogger(line => ()))
      process.exitValue() == 0
    catch
      case e: Exception => false

  def wineVersion(path: String): String = Vector(path, "--version").!!

  def check() =
    val f1 = Future {ffmpeg_installed = checkFFmpeg(ffmpeg_path)}
    val f2 = Future {ffplay_installed = checkFFmpeg(ffplay_path)}
    val f3 = Future {if system_platform != Platform.Windows then wine_installed = checkWINE()}
    val f4 = Future {if system_platform == Platform.Windows then windows_enableANSI()}
    val f5 = Future {ffprobe_installed = checkFFmpeg(ffprobe_path)}
    Await.ready(f1, Duration.Inf); Await.ready(f2, Duration.Inf); Await.ready(f3, Duration.Inf)
    Await.ready(f4, Duration.Inf); Await.ready(f5, Duration.Inf)

  def printSystemInfo(title: String) =
    val green = foreground("green"); val yellow = foreground("yellow"); val red = foreground("red"); val default = foreground("default")

    def convertBool(b: Boolean): String = if b then s"${green}yes${default}" else s"${red}no${default}"
    def colorify(s: String, color: String): String = s"$color$s$default"

    val rec_str = if system_platform == Platform.Windows then colorify("Experimental", yellow) else convertBool(recording_supported)
    val text =
      s"$title\n\nOS: $green${System.getProperty("os.name")}$default version $green${System.getProperty("os.version")}$default\n"
      + s"Arch: $green${System.getProperty("os.arch")}$default\n"
      + s"Java version: $green${System.getProperty("java.version")}$default\n"
      + s"Java class version: $green${System.getProperty("java.class.version")}$default\n"
      + s"FFmpeg installed: ${convertBool(ffmpeg_installed)}\n"
      + s"FFplay installed: ${convertBool(ffplay_installed)}\n"
      + s"FFprobe installed: ${convertBool(ffprobe_installed)}\n"
      + s"WINE enabled: ${convertBool(wine_installed)}\n"
      + s"Screenshot support: ${convertBool(recording_supported)}\n"
      + s"Video recording support: $rec_str"
    clear()
    pressToContinue(text)

