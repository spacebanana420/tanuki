package tanuki.tui

import tanuki.recorder.{rec_getOutput, rec_readConfig}
import ffscala.*
import java.io.File
import scala.util.Sorting.quickSort

def tui_movieMenu(): Unit =
  if !tui_noffmpeg() then
    val output = rec_getOutput(rec_readConfig())
    val videos = File(output)
      .list()
      .filter(x => File(s"$output/$x").isFile() && x.contains("tanuki-video"))
    quickSort(videos)

    val title = s"$green[Gensokyo cinema]$default\nChoose a video to play\n\n${green}${0}:${default} Exit\n\n"
    val video = readLoop_array(videos, title)
    if video != 0 then
      val help =
        s"\nPlaying ${videos(video-1)}...\n"
        + s"${green}Seeking:$default Arrow keys\n${green}Volume:$default 0 and 9 keys\n"
        + s"${green}Pause:$default P or space\n${green}Quit:$default Q or ESC\n"
        + s"${green}Fullscreen:$default F"
      println(help)

      val args = setAutoExit() ++ setWindowTitle("Gensokyo Cinema")
      execplay(s"$output/${videos(video-1)}", args)
      tui_movieMenu()