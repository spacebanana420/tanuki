package tanuki.tui

import tanuki.ffmpeg_installed
import tanuki.recorder.*
import tanuki.runner.*

import ffscala.*
import ffscala.capture.*
import java.io.File


def tui_configureRecording() =
  val vcodecs = List("x264")
  val acodecs = List("pcm", "opus")
  val answer = readLoop_list(vcodecs, s"Choose a video encoder\n\n${green}${0}:${default} Exit\n\n")

  if answer != 0 then
    vcodecs(answer) match
      case "x264" =>


def tui_x264Setup(): List[String] =
  val presets = List("ultrafast", "superfast", "veryfast", "medium")
  val pixfmts = List("yuv420p", "yuv422p", "yuv444p", "rgb24")

  val preset = readLoop_list(presets, s"Choose an x264 preset\n\n${green}${0}:${default} Default (superfast)\n\n")
  val crf = readLoop("Input the encoding CRF value (from 0 to 51)\nHigher value means lower quality and file size\n0 gives lossless compression", 51)
  val keyint = readLoop("Input the keyframe interval (from 1 to 600)\nLower values make it easier to decode the video, at the cost of higher bitrates in scenes that lack motion", 600)
  val pixfmt = readLoop_list(pixfmts, s"Choose an color format\n\n${green}${0}:${default} Default (yuv420p)\n\n")

  val final_preset =
    if preset == 0 then presets(1)
    else presets(preset)
  val final_pixfmt =
    if pixfmt == 0 then pixfmts(0)
    else pixfmts(pixfmt)

  List("x264", final_preset, crf.toString, keyint.toString, final_pixfmt)

def tui_pcmSetup(): List[String] =
  val depths = List("16bit", "24bit")
  val d = readLoop_list(depths, s"Choose the audio bit depth\n\n${green}${0}:${default} Default (16bit)\n\n")
  val depth =
    if d == 0 || d == 1 then "16"
    else "24"
  List("pcm", depth)

def tui_x11Setup(): List[String] =
  val w = readLoop_int("Input the capture resolution's width")
  val h = readLoop_int("Input the capture resolution's height")
  val fps = readLoop_int("Input the capture resolution's framerate")

  List("x11grab", w.toString, h.toString, fps.toString)
