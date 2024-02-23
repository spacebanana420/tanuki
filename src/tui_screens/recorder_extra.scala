package tanuki.tui

import tanuki.{ffmpeg_installed, system_platform, recording_supported}
import tanuki.recorder.*
import tanuki.runner.*

import ffscala.*
import ffscala.capture.*
import java.io.File

def setup_getBitrate(title: String): Int =
  readLoop_int(
    s"${title}Input the video bitrate (in kilobits persecond)"
    + "\nHigher means more quality and bigger file\n"
    )

private def setup_generic(title: String, firstline: String, deflt: String, presets: List[String]): String =
  val ans = readLoop_list(presets,
    s"$title$firstline\n\n${green}${0}:${default} Default ($deflt)\n\n"
  )
  if ans == 0 then deflt
  else presets(ans-1)

def setup_getPreset(title: String, encoder: String, deflt: String, presets: List[String]): String =
  val determinant =
    encoder(0) match
      case 'a' | 'e' | 'i' | 'o' | 'u' | 'x' | 'h' =>
        "an"
      case _ => "a"
  setup_generic(title, s"Choose $determinant $encoder preset", deflt, presets)

def setup_getPixfmt(title: String, deflt: String, fmts: List[String]): String =
  setup_generic(title, "Choose a color format", deflt, fmts)