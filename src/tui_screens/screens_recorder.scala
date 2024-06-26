package tanuki.tui

import tanuki.{ffmpeg_installed, system_platform, recording_supported}
import tanuki.recorder.*
import tanuki.runner.*
import tanuki.Platform
import bananatui.*
import ffscala.*
import ffscala.capture.*
import java.io.File

def tui_supportedOS(): Boolean =
  val text = "Video recording is currently only available for Windows, Linux and BSD systems!"
  if recording_supported then
    true
  else
    pressToContinue(text)
    false

def tui_recconfigerror() =
  val text = "There's an error in your video_config.txt!\nYou might have a setting that isn't configured properly, or your config isn't compatible with this version of Tanuki!\n\nWould you like to configure the video recorder now and delete the old configuration file?"
  val answer = askPrompt(text)
  if answer then tui_configureRecording()

def tui_recmissingconfig() =
  val text = "No recorder configuration has been found!\nWould you like to configure?"
  val answer = askPrompt(text)
  if answer then tui_configureRecording()

def tui_configureRecording() =
  val vcodecs = List("x264", "x264rgb", "qsv", "qsv265", "nvenc", "mjpeg", "mjpegqsv", "utvideo")
  val acodecs = List("pcm", "mp3", "aac")
  //val acodecs_mp4 = List("flac", "mp3", "opus", "aac")

  val ans_vc = chooseOption(vcodecs, s"Choose a video encoder", s"Default (x264)")
  val vcodec =
    if ans_vc != 0 then
      vcodecs(ans_vc-1) match
        case "x264" => tui_x264Setup()
        case "x264rgb" => tui_x264rgbSetup()
        case "qsv" => tui_QSVSetup()
        case "qsv265" => tui_QSV265Setup()
        case "nvenc" => tui_NVENCSetup()
        case "utvideo" => tui_utvideoSetup()
        case "mjpeg" => tui_mjpegSetup()
        case "mjpegqsv" => tui_QSVMJPEGSetup()
        case "png" => tui_pngSetup()
    else
      tui_x264Setup()
  val vcapture =
    if system_platform == Platform.Windows then
      tui_dshowSetup_video()
    else
      tui_x11Setup()
  val ans_ac = chooseOption(acodecs, s"Choose an audio encoder\n\n", s"Default (pcm)")
  val acodec =
    if ans_ac != 0 then
      acodecs(ans_ac-1) match
        case "pcm" => tui_pcmSetup()
        //case "opus" => tui_opusSetup()
        case "mp3" => tui_mp3Setup()
        case "aac" => tui_aacSetup()
    else
      tui_pcmSetup()
  val acapture =
    system_platform match
      case Platform.Windows => tui_dshowSetup_audio()
      case Platform.FreeBSD => tui_ossSetup()
      case _ => tui_pulseSetup()
      
  val crop = 
    if askPrompt("Would you like to crop your footage?") then
      tui_filterCrop()
    else
      List[String]()
  val scale =
    if askPrompt("Would you like to scale your footage?") then
      tui_filterScale()
    else
      List[String]()

  val normalize =
    if askPrompt("Would you like to normalize your audio?") then
      "loudnorm=true"
    else
      ""

  val output = chooseOption_dir("Type the path to store your video recordings (default: the current path of Tanuki)")
  val delay = readInt("Type the recording delay (in seconds)\nMax duration: 60")
  rec_writeConfig(output, delay, vcodec, acodec, vcapture, acapture, crop, scale, normalize)

////Video encoder setup////

def tui_x264Setup(): List[String] =
  val title = s"$green[x264 configuration]$default\n\n"
  val presets = List("ultrafast", "superfast", "veryfast", "medium")
  val pixfmts = List("yuv420p", "yuv422p", "yuv444p", "rgb24")

  val preset = setup_getPreset(title, "x264", "superfast", presets)
  val crf = readLoop(s"${title}Input the encoding CRF value (from 0 to 51)\nHigher value means lower quality and file size\n0 gives lossless compression\n", 51)
  val pixfmt = setup_getPixfmt(title, "yuv420p", pixfmts)

  List("x264", preset, crf.toString, pixfmt)

def tui_x264rgbSetup(): List[String] =
  val title = s"$green[x264 configuration]$default\n\n"
  val presets = List("ultrafast", "superfast", "veryfast", "medium")

  val preset = setup_getPreset(title, "x264", "superfast", presets)
  val crf = readLoop(s"${title}Input the encoding CRF value (from 0 to 51)\nHigher value means lower quality and file size\n0 gives lossless compression\n", 51)

  List("x264rgb", preset, crf.toString)

def tui_QSVSetup(): List[String] =
  val title = s"$green[QuickSync H.264 configuration]$default\n\n"
  val presets = List("veryfast", "faster", "fast", "medium", "slow", "slower", "veryslow")

  pressToContinue("You chose the QSV H.264 encoder\nThis hardware encoder requires an Intel GPU to work, so make sure that's what you're using")
  
  val bitrate = readInt(s"${title}Input the video bitrate (in kilobits persecond)\nHigher means more quality and bigger file\n")
  val preset = setup_getPreset(title, "QSV", "fast", presets)
  
  List("qsv", preset, bitrate.toString)

def tui_QSV265Setup(): List[String] =
  val title = s"$green[QuickSync H.265 configuration]$default\n\n"
  val presets = List("veryfast", "faster", "fast", "medium", "slow", "slower", "veryslow")
  val pixfmts = List("nv12", "yuyv422")

  pressToContinue("You chose the QSV H.265 encoder\nThis hardware encoder requires an Intel GPU to work, so make sure that's what you're using")
  
  val bitrate = readInt(s"${title}Input the video bitrate (in kilobits persecond)\nHigher means more quality and bigger file\n")
  val preset = setup_getPreset(title, "QSV", "faster", presets)
  val pixfmt = setup_getPixfmt(title, "yuyv422", pixfmts)

  List("qsv265", preset, bitrate.toString, pixfmt)

def tui_QSVMJPEGSetup(): List[String] =
  val title = s"$green[QuickSync MJPEG configuration]$default\n\n"
  val pixfmts = List("nv12", "yuyv422")
  pressToContinue("You chose the QSV MJPEG encoder\nThis hardware encoder requires an Intel GPU to work, so make sure that's what you're using")
  
  val q = readByte(s"${title}Input the video quality (from 1 to 100)\nHigher means more quality and bigger file\n")
  val pixfmt = setup_getPixfmt(title, "yuyv422", pixfmts)
  val quality = if q < 1 then 1 else if q > 100 then 100 else q 

  List("mjpegqsv", quality.toString, pixfmt)

def tui_NVENCSetup(): List[String] =
  val title = s"$green[NVENC H.264 configuration]$default\n\n"
  val presets = List("fast", "medium", "slow", "slower")
  val true_presets = List("p3", "p4", "p5", "p6")
  val pixfmts = List("yuv420p", "yuv422p", "yuv444p", "rgb0")
  pressToContinue("You chose the NVENC H.264 encoder"
  +"\nThis hardware encoder requires an NVIDIA GPU to work, so make sure that's what you're using")

  val preset = chooseOption(presets, s"${title}Choose an NVENC preset", s"Default (medium)")
  val bitrate = setup_getBitrate(title)
  val pixfmt = setup_getPixfmt(title, "yuv420p", pixfmts)

  val final_preset =
    if preset == 0 then true_presets(1)
    else true_presets(preset-1)

  List("nvenc", final_preset, bitrate.toString, pixfmt)

def tui_utvideoSetup(): List[String] =
  val title = s"$green[Utvideo configuration]$default\n\n"
  val pixfmts = List("yuv420p", "yuv422p", "yuv444p", "gbrp")

  val pixfmt = setup_getPixfmt(title, "gbrp", pixfmts)
  List("utvideo", pixfmt)

def tui_mjpegSetup(): List[String] =
  val title = s"$green[MJPEG configuration]$default\n\n"
  val pixfmts = List("yuvj420p", "yuvj422p", "yuvj444p")
  val ask: String =
    s"${title}Input the quality value (from 1 to 120) (Default: 1)" +
    s"\n\nHigher value means lower quality and file size"

  val quality = readShort(ask)
  val pixfmt = setup_getPixfmt(title, "yuvj444p", pixfmts)
  
  val final_quality: Short =
    if quality == 0 then 1
    else if quality > 120 then 120
    else quality

  List("mjpeg", final_quality.toString, pixfmt)

def tui_pngSetup(): List[String] =
  val title =
    s"$green[PNG configuration]$default\n\n" +
    s"Choose the comrpession quality" +
    s"\nThis does not affect picture quality, but does affect CPU usage and file size"
  val qualities = List("Low", "Medium", "High")

  pressToContinue("Warning: PNG encoding is heavy, and video file sizes are very large\nIt's ideal to record at lower framerates or resolutions")
  val quality = chooseOption(qualities, title, s"Default (high)")
  val final_quality =
    if quality == 0 then 2
    else quality - 1

  List("png", final_quality.toString)

////Audeo encoder setup////

def tui_pcmSetup(): List[String] =
  val title = s"$green[Audio configuration]$default\n\n"
  val depths = List("16bit", "24bit")
  val d = chooseOption(depths, s"${title}Choose the audio bit depth", s"Default (16bit)")
  val depth =
    if d == 0 || d == 1 then "16"
    else "24"
  List("pcm", depth)

def tui_opusSetup(): List[String] =
  val title = s"$green[Audio configuration]$default\n\n"
  val bitrate = readInt(s"${title}Input the audio bitrate (in kilobits per second)")
  List("opus", bitrate.toString)

def tui_mp3Setup(): List[String] =
  val title = s"$green[Audio configuration]$default\n\n"
  val bitrate = readInt(s"${title}Input the audio bitrate (in kilobits per second)")
  List("mp3", bitrate.toString)

def tui_aacSetup(): List[String] =
  val title = s"$green[Audio configuration]$default\n\n"
  val bitrate = readInt(s"${title}Input the audio bitrate (in kilobits per second)")
  List("aac", bitrate.toString)

////Capture setup////

def tui_x11Setup(): List[String] =
  val title = s"$green[Video capture]$default\n\n"

  val w = readInt(s"${title}Input the capture resolution's width")
  val h = readInt(s"${title}Input the capture resolution's height")
  val fps = readInt(s"${title}Input the capture resolution's framerate")
  val safemode =
    if fps != 60 then
      askPrompt(
      "Would you like to enable safe mode?\n" +
      "Enable this if you are not recording at 60FPS but the footage has extreme stutters"
      )
    else false

  List("x11grab", w.toString, h.toString, fps.toString, safemode.toString)

def tui_pulseSetup(): List[String] =
  val title = s"$green[Audio capture]$default\n\n"
  
  val sources = getSources_pulse()
  val ans = chooseOption(sources, s"${title}Choose the audio input source to use", s"Default (system's default)")
  val input =
    if ans == 0 then "default"
    else sources(ans-1)
  List("pulse", input)

def tui_ossSetup(): List[String] = //merge with the one above
  val title = s"$green[Audio capture]$default\n\n"
  
  val sources = getSources_oss()
  val ans = chooseOption(sources, s"${title}Choose the audio input source to use", s"Default (system's default)")
  val input =
    if ans == 0 then "default"
    else sources(ans-1)
  List("oss", input)
 
def tui_dshowSetup_video(): List[String] =
  val title = s"$green[Video capture]$default\n\n"
  val sources = getSources_dshow_v()

  val ans = chooseOption(sources, s"${title}Choose the video input source to use", s"Default (${sources(0)})")
  val w = readInt(s"${title}Input the capture resolution's width")
  val h = readInt(s"${title}Input the capture resolution's height")
  val fps = readInt(s"${title}Input the capture resolution's framerate")

  val input =
    if ans == 0 then sources(0)
    else sources(ans-1)
  List("dshow", input, w.toString, h.toString, fps.toString)

def tui_dshowSetup_audio(): List[String] =
  val title = s"$green[Video capture]$default\n\n"
  val sources = getSources_dshow_a()

  val ans = chooseOption(sources, s"${title}Choose the audio input source to use", s"Default (${sources(0)})")
  val input =
    if ans == 0 then sources(0)
    else sources(ans-1)
  List("dshow", input)

////Filter setup////

def tui_filterCrop(): List[String] =
  val title = s"$green[Crop filter]$default\n\n"
  val w = readInt("Input the crop width")
  val h = readInt("Input the crop height")
  List(w.toString, h.toString)

def tui_filterScale(): List[String] =
  val title = s"$green[Scale filter]$default\n\n"
  val w = readInt("Input the scale width")
  val h = readInt("Input the scale height")
  List(w.toString, h.toString)
