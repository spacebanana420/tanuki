package tanuki

import tanuki.tui.*
import tanuki.config.*
import ffscala.checkFFmpeg

val ffmpeg_installed = checkFFmpeg()

@main def main() =
  if !configExists() then
    createConfig()
  if isConfigOk() then
    tui_title()
  else
    tui_configerror()
