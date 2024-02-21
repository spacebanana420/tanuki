package tanuki

import tanuki.tui.*
import tanuki.config.*
import ffscala.checkFFmpeg
import java.io.File

val ffmpeg_installed = checkFFmpeg()
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
