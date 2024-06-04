package tanuki

import tanuki.tui.*
import tanuki.config.*
import ffscala.checkFFmpeg
import java.io.File

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val ffmpeg_path = platformcheck.getFFmpeg("ffmpeg")
val ffplay_path = platformcheck.getFFmpeg("ffplay")
var ffmpeg_installed = false
var ffplay_installed = false //handled by platformcheck

val system_platform = platformcheck.getPlatform()
val recording_supported = system_platform != 3

@main def main() =
  platformcheck.check()
  if !configExists() then createConfig()
  if isConfigOk() then
    tui_title()
  else
    tui_configerror()

object platformcheck:
  def getPlatform(): Byte =
    val platform = System.getProperty("os.name")
    if platform.contains("Windows") then 0
    else if platform.contains("Linux") then if File("/nix/store").isDirectory() then 1 else 2
    else if platform.contains("Mac") then 3
    else if platform.contains("FreeBSD") then 4
    else 5

  def getFFmpeg(exec: String): String =
    def findExec(dir: String, files: Array[String], i: Int = 0): String =
      if i >= files.length then ""
      else if files(i).contains(exec) && File(s"$dir/${files(i)}").canExecute() then
        File(s"$dir/${files(i)}").getAbsolutePath()
      else findExec(dir, files, i+1)

    if configExists() then
      val path = getFFmpegPath(readConfig())
      if File(path).isDirectory() then
        val ffpath = findExec(path, File(path).list())
        if ffpath == "" then exec else ffpath
      else exec
    else exec

  def check() =
    var peg_done = false; var play_done = false
    Future {
      ffmpeg_installed = checkFFmpeg(ffmpeg_path)
      peg_done = true
    }
    Future {
      ffplay_installed = checkFFmpeg(ffplay_path)
      play_done = true
    }
    while !peg_done && !play_done do Thread.sleep(20)
