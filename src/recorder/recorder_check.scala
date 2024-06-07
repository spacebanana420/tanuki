package tanuki.recorder

import tanuki.{recording_supported, ffmpeg_installed}
import tanuki.tui.{tui_noffmpeg, tui_supportedOS, tui_recmissingconfig, tui_recconfigerror}
import tanuki.misc.similarInList

import java.io.File

def rec_isRecordingSupported(): Boolean = //this is a mess, rework this later
  def everythingOk(i: Int = 0): Boolean =
    if i >= 3 then
      true
    else
      val ok =
        i match
          case 0 => !tui_noffmpeg()
          case 1 => rec_configExists()
          case 2 => tui_supportedOS()

      if ok then everythingOk(i+1)
      else
        if i == 1 then tui_recmissingconfig()
        false

  if everythingOk() && rec_isConfigOk() then
    true
  else
    false

def rec_isConfigOk(config: Seq[String] = List()): Boolean =
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
        if vcodec.length == 4 then true else false
      case "x264rgb" =>
        vcodec(2).toByte;
        if vcodec.length == 3 then true else false
      case "qsv" =>
        vcodec(2).toInt
        if vcodec.length == 3 then true else false
      case "qsv265" =>
        vcodec(2).toInt
        if vcodec.length == 4 then true else false
      case "nvenc" =>
        vcodec(2).toInt
        if vcodec.length == 4 then true else false
      case "mjpegqsv" =>
        vcodec(1).toByte
        if vcodec.length == 3 then true else false
      case "utvideo" =>
        if vcodec.length == 2 then true else false
      case "mjpeg" =>
        vcodec(1).toShort
        if vcodec.length == 3 then true else false
      case "png" =>
        vcodec(1).toByte
        if vcodec.length == 2 then true else false
      case _ => false
  catch
    case e: Exception => false

private def acodec_ok(cfg: Seq[String]): Boolean =
  val acodec = rec_getacodec(cfg)
  try
    acodec(0) match
      case "pcm" =>
        acodec(1).toByte
        if acodec.length == 2 then true else false
      // case "opus" =>
      //   acodec(1).toInt
      //   if acodec.length == 2 then true else false
      case "mp3" =>
        acodec(1).toInt
        if acodec.length == 2 then true else false
      case "aac" =>
        acodec(1).toInt
        if acodec.length == 2 then true else false
      case _ => false
  catch case e: Exception => false

private def vcapture_ok(cfg: Seq[String]): Boolean =
  val vcapture = rec_getvcapture(cfg)
  try
    vcapture(0) match
      case "x11grab" =>
        vcapture(1).toInt; vcapture(2).toInt; vcapture(3).toInt; vcapture(4).toBoolean
        if vcapture.length == 5 then true else false
      case "dshow" =>
        vcapture(2).toInt; vcapture(3).toInt; vcapture(4).toInt
        if vcapture.length == 5 then true else false
      case _ => false
  catch
    case e: Exception => false

private def acapture_ok(cfg: Seq[String]): Boolean =
  val acapture = rec_getacapture(cfg)
  try
    acapture(0) match
      case "pulse" | "oss" =>
        if acapture.length == 2 then true else false
      case "dshow" =>
        if acapture.length == 2 then true else false
      case _ => false
  catch
    case e: Exception => false


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
    case e: Exception => false
