package tanuki.tui

import tanuki.ffmpeg_installed
import tanuki.recorder.*
import tanuki.runner.*

import ffscala.*
import ffscala.capture.*
import java.io.File

def tui_recconfigerror() =
  val text = "There's an error in your video_config.txt!\nYou might have a setting that isn't configured properly, or your config isn't compatible with this version of Tanuki!\n\nWould you like to configure the video recorder now and delete the old configuration file?"
  val answer = askPrompt(text)
  if answer then tui_configureRecording()

def tui_recmissingconfig() =
  val text = "No recorder configuration has been found!\nWould you like to configure?"
  val answer = askPrompt(text)
  if answer then tui_configureRecording()

def tui_configureRecording() =
  val vcodecs = List("x264")
  val acodecs = List("pcm", "opus")

  val ans_vc = readLoop_list(vcodecs, s"Choose a video encoder\n\n${green}${0}:${default} Default (x264)\n\n")
  val ans_ac = readLoop_list(acodecs, s"Choose an audio encoder\n\n${green}${0}:${default} Default (pcm)\n\n")

  val vcodec =
    if ans_vc != 0 then
      vcodecs(ans_vc-1) match //only x264 for now
        case "x264" => tui_x264Setup()
    else
      tui_x264Setup()
  val acodec =
    if ans_ac != 0 then
      acodecs(ans_ac-1) match
        case "pcm" => tui_pcmSetup()
        case "opus" => tui_opusSetup()
    else
      tui_pcmSetup()
  val vcapture = tui_x11Setup()
  val acapture = tui_pulseSetup()

  val output = readLoop_dir("Type the path to store your video recordings (default: the current path of Tanuki)")
  val delay = readLoop_int("Type the recording delay (in seconds)\nMax duration: 60")
  rec_writeConfig(output, delay, vcodec, acodec, vcapture, acapture)

def tui_x264Setup(): List[String] =
  val presets = List("ultrafast", "superfast", "veryfast", "medium")
  val pixfmts = List("yuv420p", "yuv422p", "yuv444p", "rgb24")

  val preset = readLoop_list(presets, s"Choose an x264 preset\n\n${green}${0}:${default} Default (superfast)\n\n")
  val crf = readLoop("Input the encoding CRF value (from 0 to 51)\nHigher value means lower quality and file size\n0 gives lossless compression", 51)
  val keyint = readLoop("Input the keyframe interval (from 1 to 600)\nLower values make it easier to decode the video, at the cost of higher bitrates in scenes that lack motion", 600)
  val pixfmt = readLoop_list(pixfmts, s"Choose a color format\n\n${green}${0}:${default} Default (yuv420p)\n\n")

  val final_preset =
    if preset == 0 then presets(1)
    else presets(preset-1)
  val final_pixfmt =
    if pixfmt == 0 then pixfmts(0)
    else pixfmts(pixfmt-1)

  List("x264", final_preset, crf.toString, keyint.toString, final_pixfmt)

def tui_pcmSetup(): List[String] =
  val depths = List("16bit", "24bit")
  val d = readLoop_list(depths, s"Choose the audio bit depth\n\n${green}${0}:${default} Default (16bit)\n\n")
  val depth =
    if d == 0 || d == 1 then "16"
    else "24"
  List("pcm", depth)

def tui_opusSetup(): List[String] =
  val bitrate = readLoop_int("Input the audio bitrate (in kilobits per second)")
  List("opus", bitrate.toString)

def tui_x11Setup(): List[String] =
  val w = readLoop_int("Input the capture resolution's width")
  val h = readLoop_int("Input the capture resolution's height")
  val fps = readLoop_int("Input the capture resolution's framerate")

  List("x11grab", w.toString, h.toString, fps.toString)

def tui_pulseSetup(): List[String] =
  val sources = getSources_pulse()
  val ans = readLoop_list(sources, s"Choose the audio input source to use\n\n${green}${0}:${default} Default (system's default))\n\n")
  val input =
    if ans == 0 then "default"
    else sources(ans-1)
  List("pulse", input)
