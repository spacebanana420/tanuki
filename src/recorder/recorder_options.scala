package tanuki.recorder

import ffscala.*
import ffscala.capture.*

def supportedCodecs(): List[String] =
  List
    (
    "x264", "utvideo", "cfhd", "mjpeg"
    )

def supportedColor(): List[String] =
  List
    (
    "rgb24", "yuv420p", "yuv422p", "yuv444p"
    )

/////VIDEO/////

def video_setx264(preset: String, crf: Byte, keyInt: Short, pixfmt: String): List[String] =
  x264_setPreset(preset) ++ setCRF(crf) ++ setKeyframeInterval(keyInt) ++ setPixFmt(pixfmt)

/////VIDEO CAPTURE/////

def capture_x11(w: Int, h: Int, fps: Int): List[String] =
  x11grab_captureVideo("0.0", fps, false, width = w, height = h)

/////AUDIO/////

def audio_setPCM(depth: Byte): List[String] =
  depth match
    case 16 => setAudioEncoder("pcm16")
    case 24 => setAudioEncoder("pcm24")
    case _ => setAudioEncoder("pcm16")

def audio_setOpus(bitrate: Int): List[String] =
  setAudioEncoder("opus") ++ setAudioBitrate(bitrate)

/////AUDIO CAPTURE/////

def getSources_pulse(): List[String] = listSources("pulse")

def capture_pulse(input: String): List[String] = captureAudio("pulse", input)
