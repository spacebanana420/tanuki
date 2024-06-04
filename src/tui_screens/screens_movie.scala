package tanuki.tui

import tanuki.{ffmpeg_path, ffplay_path}
import tanuki.recorder.{rec_getOutputArg}

import bananatui.*
import ffscala.*
import java.io.File
import scala.util.Sorting.quickSort

def tui_movieMenu(): Unit =
  if !tui_noffplay() then
    val output = rec_getOutputArg()
    val videos = File(output)
      .list()
      .filter(x => File(s"$output/$x").isFile() && x.contains("tanuki-video"))
    quickSort(videos)

    if videos.length == 0 then
      pressToContinue(s"No video files have been found in the directory $output!")
    else
      val t = s"$green[Gensokyo cinema]$default\nChoose a video to play"
      val video = chooseOption_h(videos.toVector, size = 2, title = t) //replace toVector, it lowers speed
      if video != 0 then
        println(
          s"\nPlaying $green${videos(video-1)}$default...\n\n"
          + s"${green}Seeking:$default Arrow keys\n${green}Volume:$default 0 and 9 keys\n"
          + s"${green}Pause:$default P or space\n${green}Quit:$default Q or ESC\n"
          + s"${green}Fullscreen:$default F"
        )
        //add ffmpeg killer to force quit

        val args = setAutoExit() ++ setWindowTitle("Gensokyo Cinema")
        execplay(s"$output/${videos(video-1)}", args, exec=ffplay_path)
        tui_movieMenu()
