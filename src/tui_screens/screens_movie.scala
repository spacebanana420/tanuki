package tanuki.tui

import tanuki.{ffmpeg_path, ffplay_path}
import tanuki.recorder.{rec_getOutputArg}
import ffscala.*
import java.io.File
import scala.util.Sorting.quickSort

def tui_movieMenu(): Unit =
  if !tui_noffmpeg() then
    val output = rec_getOutputArg()
    val videos = File(output)
      .list()
      .filter(x => File(s"$output/$x").isFile() && x.contains("tanuki-video"))
    quickSort(videos)

    val title = s"$green[Gensokyo cinema]$default\nChoose a video to play\n\n${green}${0}:${default} Exit\n\n"
    val video = readLoop_movie(videos, title)
    if video != 0 then
      println(
        s"\nPlaying ${videos(video-1)}...\n"
        + s"${green}Seeking:$default Arrow keys\n${green}Volume:$default 0 and 9 keys\n"
        + s"${green}Pause:$default P or space\n${green}Quit:$default Q or ESC\n"
        + s"${green}Fullscreen:$default F"
      )

      val args = setAutoExit() ++ setWindowTitle("Gensokyo Cinema")
      execplay(s"$output/${videos(video-1)}", args, exec=ffplay_path)
      tui_movieMenu()