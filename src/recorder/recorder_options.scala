package tanuki.recorder

import ffscala.*
import ffscala.capture.*

// def supportedCodecs(): List[String] =
//   List
//     (
//     "x264", "utvideo", "cfhd", "mjpeg"
//     )
//
// def supportedColor(): List[String] =
//   List
//     (
//     "rgb24", "yuv420p", "yuv422p", "yuv444p"
//     )

/////VIDEO/////

def video_setx264(preset: String, crf: Byte, pixfmt: String): List[String] =
  setVideoEncoder("x264") ++ x264_setPreset(preset) ++ setCRF(crf) ++ setPixFmt(pixfmt)

def video_setx264rgb(preset: String, crf: Byte): List[String] =
  setVideoEncoder("x264rgb") ++ x264_setPreset(preset) ++ setCRF(crf) //++ setPixFmt("rgb24")

def video_setQSV(preset: String, bitrate: Int): List[String] =
  setVideoEncoder("qsv") ++ qsv_setPreset(preset) ++ setVideoBitrate(bitrate) ++ setPixFmt("nv12")

def video_setQSV265(preset: String, bitrate: Int, pixfmt: String): List[String] =
  setVideoEncoder("qsv265") ++ qsv_setPreset(preset) ++ setVideoBitrate(bitrate) ++ setPixFmt(pixfmt)

def video_setUtvideo(pixfmt: String): List[String] =
  setVideoEncoder("utvideo") ++ setPixFmt(pixfmt)

def video_setMjpeg(quality: Short, pixfmt: String): List[String] =
  setVideoEncoder("mjpeg") ++ setQuality(quality) ++ setPixFmt(pixfmt)

/////AUDIO/////

def audio_setPCM(depth: Byte): List[String] =
  depth match
    case 16 => setAudioEncoder("pcm16")
    case 24 => setAudioEncoder("pcm24")
    case _ => setAudioEncoder("pcm16")

def audio_setOpus(bitrate: Int): List[String] =
  setAudioEncoder("opus") ++ setAudioBitrate(bitrate)

def audio_setmp3(bitrate: Int): List[String] =
  setAudioEncoder("mp3") ++ setAudioBitrate(bitrate)

/////CAPTURE/////

def getSources_dshow_v(): List[String] = listSources("dshow_video")

def getSources_pulse(): List[String] = listSources("pulse")

def getSources_dshow_a(): List[String] = listSources("dshow_audio")

def capture_x11(w: Int, h: Int, fps: Int): List[String] =
  x11grab_captureVideo("0.0", fps, false, width = w, height = h)

def capture_dshow_v(input: String, w: Int, h: Int, fps: Int): List[String] =
  dshow_captureVideo(input, fps, w, h)

def capture_dshow_a(input: String): List[String] =
  dshow_captureAudio(input, 48000, 2, 24) //improve on ffscala

def capture_pulse(input: String): List[String] = captureAudio("pulse", input)

/////RECORD FILTERS/////

def tanukifilter_crop(w: Int, h: Int): List[String] = cropCenter(w, h)

def tanukifilter_scale(w: Int, h: Int): List[String] = scale(w, h)
