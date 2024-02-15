package tanuki

import tanuki.tui.*
import tanuki.config.*
import ffscala.checkFFmpeg

val ffmpeg_installed = checkFFmpeg()
//val system_platform = getPlatform()

@main def main() =
  if !configExists() then
    createConfig()
  if isConfigOk() then
    tui_title()
  else
    tui_configerror()


// def getPlatform(): Byte =
//   if File("C:").isDirectory() then 0
//   else if ()
