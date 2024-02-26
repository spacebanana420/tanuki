package tanuki

import tanuki.tui.*
import tanuki.config.*
import ffscala.checkFFmpeg
import java.io.File

val ffmpeg_path = getFFmpeg("ffmpeg")
val ffplay_path = getFFmpeg("ffplay")
val ffmpeg_installed = checkFFmpeg(ffmpeg_path)

val system_platform = getPlatform()
val recording_supported = system_platform < 3

@main def main() =
  if !configExists() then
    createConfig()
  if isConfigOk() then
    tui_title()
  else
    tui_configerror()

def getPlatform(): Byte =
  if File("C:").isDirectory() then 0
  else if File("/nix/store").isDirectory() then 1
  else if File("/run").isDirectory() && File("/bin").isDirectory() then 2
  else 3

def getFFmpeg(exec: String): String =
  def findExec(dir: String, files: Array[String], i: Int = 0): String =
    if i >= files.length then ""
    else if files(i).contains(exec) && File(s"$dir/${files(i)}").canExecute() then
      File(s"$dir/${files(i)}").getAbsolutePath()
    else findExec(dir, files, i+1)
    
  val path = getFFmpegPath(readConfig())
  if File(path).isDirectory() then
    val ffpath = findExec(path, File(path).list())
    if ffpath == "" then exec else ffpath
  else exec
