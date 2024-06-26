package tanuki.recorder

import ffscala.*
import ffscala.capture.*

/////VIDEO/////

def video_setx264(preset: String, crf: Byte, pixfmt: String): List[String] =
  setVideoEncoder("x264") ++ x264_setPreset(preset) ++ setCRF(crf) ++ setPixFmt(pixfmt)

def video_setx264rgb(preset: String, crf: Byte): List[String] =
  setVideoEncoder("x264rgb") ++ x264_setPreset(preset) ++ setCRF(crf)

def video_setQSV(preset: String, bitrate: Int): List[String] =
  setVideoEncoder("qsv") ++ qsv_setPreset(preset) ++ setVideoBitrate(bitrate) ++ setPixFmt("nv12")

def video_setQSV265(preset: String, bitrate: Int, pixfmt: String): List[String] =
  setVideoEncoder("qsv265") ++ qsv_setPreset(preset) ++ setVideoBitrate(bitrate) ++ setPixFmt(pixfmt)

def video_setQSVMJPEG(quality: Byte, pixfmt: String): List[String] =
  setVideoEncoder("mjpegqsv") ++ qsv_MJPEGQuality(quality) ++ setPixFmt(pixfmt)

def video_setNVENC(preset: String, bitrate: Int, pixfmt: String): List[String] =
  setVideoEncoder("nvenc") ++ nvenc_setPreset(preset) ++ setVideoBitrate(bitrate) ++ setPixFmt(pixfmt)

def video_setUtvideo(pixfmt: String): List[String] =
  setVideoEncoder("utvideo") ++ setPixFmt(pixfmt)

def video_setMjpeg(quality: Short, pixfmt: String): List[String] =
  setVideoEncoder("mjpeg") ++ setQuality(quality) ++ setPixFmt(pixfmt)

def video_setPNG(pred: Byte): List[String] =
  val pred_arg = pred match
    case 0 => "none"
    case 1 => "up"
    case _ =>  "mixed"
  setVideoEncoder("png") ++ png_setPred(pred_arg) ++ setPixFmt("rgb24")

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

def audio_setaac(bitrate: Int): List[String] =
  setAudioEncoder("aac") ++ setAudioBitrate(bitrate)

// def audio_setflac(): List[String] = setAudioEncoder("flac")

/////CAPTURE/////

// def getSources_dshow_v(): List[String] = listSources("dshow_video")

def getSources_pulse(): List[String] = listSources("pulse")

def getSources_oss(): List[String] = listSources_OSS().toList //temporary list conversion, not ideal

def getSources_dshow_a(): List[String] = listSources("dshow_audio")

def capture_x11(w: Int, h: Int, fps: Int, safemode: Boolean): List[String] =
  if safemode then
    x11grab_captureVideo("0.0", 60, false, width = w, height = h)
  else
    x11grab_captureVideo("0.0", fps, false, width = w, height = h)

def capture_windows_v(w: Int, h: Int, fps: Int): List[String] =
  gdigrab_captureVideo(fps, width = w, height = h)

def capture_windows_a(input: String): List[String] =
  dshow_captureAudio(input, 48000, 2, 24) //improve on ffscala

def capture_pulse(input: String): List[String] = captureAudio("pulse", input)

def capture_oss(input: String): List[String] = oss_captureAudio(input)

/////RECORD FILTERS/////

def tanukifilter_crop(w: Int, h: Int): List[String] = cropCenter(w, h)

def tanukifilter_scale(w: Int, h: Int): List[String] = scale(w, h)

def tanukifilter_normalize(): List[String] = normalizeAudio()
