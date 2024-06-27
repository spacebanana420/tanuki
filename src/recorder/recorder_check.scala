package tanuki.recorder

import tanuki.{recording_supported, ffmpeg_installed}
import tanuki.tui.{tui_noffmpeg, tui_supportedOS, tui_recmissingconfig, tui_recconfigerror}
import tanuki.misc.similarInList
import bananatui.*

import java.io.File

enum rec_issue:
  case no_ffmpeg, no_config, is_macos, config_not_ok, allclear

def reportRecordingIssue(issue: rec_issue) =
    if issue == rec_issue.no_ffmpeg then tui_noffmpeg()
    else if issue == rec_issue.no_config then tui_recmissingconfig()
    else if issue == rec_issue.is_macos then pressToContinue("Unfortunately, MacOS is not supported by Tanuki's screen recorder")
    else if issue == rec_issue.config_not_ok then pressToContinue("Your video configuration is incorrect! Read the error messages above to see what's wrong")

def rec_isRecordingSupported(cfg: Seq[String] = Vector(), report: Boolean = true): Boolean =
  val status =
    if !recording_supported then rec_issue.is_macos
    else if !ffmpeg_installed then rec_issue.no_ffmpeg
    else if !rec_configExists() then rec_issue.no_config
    else if !rec_isConfigOk(cfg) then rec_issue.config_not_ok
    else rec_issue.allclear

  if report then reportRecordingIssue(status)
  status == rec_issue.allclear

def rec_isConfigOk(config: Seq[String] = Vector()): Boolean =
  val cfg =
    if config.length == 0 then
      rec_readConfig()
    else
      config
  if output_ok(cfg) && vcodec_ok(cfg) && acodec_ok(cfg) && vcapture_ok(cfg) && acapture_ok(cfg) && filters_ok(cfg) then
    true
  else
    false

private def output_ok(cfg: Seq[String]): Boolean =
  val output = rec_getOutput(cfg)
  File(output).isDirectory()

private def vcodec_ok(cfg: Seq[String]): Boolean =
  val vcodec = rec_getvcodec(cfg)
  try
    vcodec(0) match
      case "x264" =>
        vcodec(2).toByte;
        if vcodec.length == 4 then true
        else
          printStatus("x264 configuration error: wrong length or type of arguments")
          false
      case "x264rgb" =>
        vcodec(2).toByte;
        if vcodec.length == 3 then true
        else
          printStatus("x264rgb configuration error: wrong length or type of arguments")
          false
      case "qsv" =>
        vcodec(2).toInt
        if vcodec.length == 3 then true
        else
          printStatus("qsv configuration error: wrong length or type of arguments")
          false
      case "qsv265" =>
        vcodec(2).toInt
        if vcodec.length == 4 then true
        else
          printStatus("qsv265 configuration error: wrong length or type of arguments")
          false
      case "nvenc" =>
        vcodec(2).toInt
        if vcodec.length == 4 then true
        else
          printStatus("nvenc configuration error: wrong length or type of arguments")
          false
      case "mjpegqsv" =>
        vcodec(1).toByte
        if vcodec.length == 3 then true
        else
          printStatus("mjpegqsv configuration error: wrong length or type of arguments")
          false
      case "utvideo" =>
        if vcodec.length == 2 then true
        else
          printStatus("utvideo configuration error: wrong length or type of arguments")
          false
      case "mjpeg" =>
        vcodec(1).toShort
        if vcodec.length == 3 then true
        else
          printStatus("mjpeg configuration error: wrong length or type of arguments")
          false
      case "png" =>
        vcodec(1).toByte
        if vcodec.length == 2 then true
        else
          printStatus("png configuration error: wrong length or type of arguments")
          false
      case _ =>
        printStatus("Video configuration error: video encoding parameters are not configured")
        false
  catch
    case e: Exception =>
      printStatus("Video configuration error: video encoding parameters are not configured")
      false

private def acodec_ok(cfg: Seq[String]): Boolean =
  val acodec = rec_getacodec(cfg)
  try
    acodec(0) match
      case "pcm" =>
        acodec(1).toByte
        if acodec.length == 2 then true
        else
          printStatus("pcm configuration error: wrong length or type of arguments")
          false
      // case "opus" =>
      //   acodec(1).toInt
      //   if acodec.length == 2 then true else false
      case "mp3" =>
        acodec(1).toInt
        if acodec.length == 2 then true
        else
          printStatus("mp3 configuration error: wrong length or type of arguments")
          false
      case "aac" =>
        acodec(1).toInt
        if acodec.length == 2 then true
        else
          printStatus("aac configuration error: wrong length or type of arguments")
          false
      case _ =>
        printStatus("Audio configuration error: audio encoding parameters are not configured")
        false
  catch case e: Exception =>
    printStatus("Audio configuration error: audio encoding parameters are not configured")
    false

private def vcapture_ok(cfg: Seq[String]): Boolean =
  val vcapture = rec_getvcapture(cfg)
  try
    vcapture(0) match
      case "x11grab" =>
        vcapture(1).toInt; vcapture(2).toInt; vcapture(3).toInt; vcapture(4).toBoolean
        if vcapture.length == 5 then true
        else
          printStatus("x11grab configuration error: wrong length or type of arguments")
          false
      case "gdigrab" =>
        vcapture(1).toInt; vcapture(2).toInt; vcapture(3).toInt
        if vcapture.length == 4 then true
        else
          printStatus("gdigrab configuration error: wrong length or type of arguments")
          false
      case _ =>
        printStatus("Capture configuration error: screen capture is not configured")
        false
  catch
    case e: Exception =>
      printStatus("Capture configuration error: screen capture is not configured")
      false

private def acapture_ok(cfg: Seq[String]): Boolean =
  val acapture = rec_getacapture(cfg)
  try
    acapture(0) match
      case "pulse" | "oss" =>
        if acapture.length == 2 then true
        else
          printStatus("pulse/oss configuration error: wrong length or type of arguments")
          false
      case "dshow" =>
        if acapture.length == 2 then true
        else
          printStatus("dshow configuration error: wrong length or type of arguments")
          false
      case _ =>
        printStatus("Capture configuration error: audio capture is not configured")
        false
  catch
    case e: Exception =>
      printStatus("Capture configuration error: audio capture is not configured")
      false


private def filters_ok(cfg: Seq[String]): Boolean =
  val crop = rec_getCrop(cfg)
  val scale = rec_getScale(cfg)
  try
    if crop.length != 0 then
      crop(0).toInt; crop(1).toInt
    if scale.length != 0 then
      scale(0).toInt; scale(1).toInt
    true
  catch
    case e: Exception =>
      printStatus("Filter configuration error: wrong length or type of arguments")
      false
