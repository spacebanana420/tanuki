package tanuki.recorder

import tanuki.misc.similarInList

import ffscala.setFramerate
import java.io.File
import java.io.FileOutputStream
import scala.io.Source

def rec_getCaptureArgs(config: Seq[String] = List()): List[String] =
  val cfg =
    if config.length == 0 then rec_readConfig()
    else config

  val vcapture = rec_getvcapture(cfg)
  val acapture = rec_getacapture(cfg)

  val vcapture_args =
    if vcapture(0) == "gdigrab" then
      capture_windows_v(vcapture(1).toInt, vcapture(2).toInt, vcapture(3).toInt)
    else
      capture_x11(vcapture(1).toInt, vcapture(2).toInt, vcapture(3).toInt, vcapture(4).toBoolean)
  val acapture_args =
    acapture(0) match
      case "dshow" =>
        capture_windows_a(acapture(1))
      case "pulse" =>
        capture_pulse(acapture(1))
      case _ =>
        capture_oss(acapture(1))

  vcapture_args ++ acapture_args

def rec_getEncodeArgs(config: Seq[String] = List()): List[String] =
  val cfg =
    if config.length == 0 then rec_readConfig()
    else config
  val vcodec = rec_getvcodec(cfg)
  val acodec = rec_getacodec(cfg)

  val v_args =
    vcodec(0) match
    case "x264" =>
      video_setx264(vcodec(1), vcodec(2).toByte, vcodec(3))
    case "x264rgb" =>
      video_setx264rgb(vcodec(1), vcodec(2).toByte)
    case "qsv" =>
      video_setQSV(vcodec(1), vcodec(2).toInt)
    case "qsv265" =>
      video_setQSV265(vcodec(1), vcodec(2).toInt, vcodec(3))
    case "nvenc" =>
      video_setNVENC(vcodec(1), vcodec(2).toInt, vcodec(3))
    case "mjpegqsv" => 
      video_setQSVMJPEG(vcodec(1).toByte, vcodec(2))
    case "utvideo" =>
      video_setUtvideo(vcodec(1))
    case "mjpeg" =>
      video_setMjpeg(vcodec(1).toShort, vcodec(2))
    case "png" =>
      video_setPNG(vcodec(0).toByte)
    case _ => List[String]()
  val a_args =
    acodec(0) match
      case "pcm" =>
        audio_setPCM(acodec(1).toByte)
      // case "opus" =>
      //   audio_setOpus(acodec(1).toInt)
      case "mp3" =>
        audio_setmp3(acodec(1).toInt)
      case "aac" =>
        audio_setaac(acodec(1).toInt)
      case _ => List[String]()

  v_args ++ a_args

def rec_getFilterArgs(config: Seq[String] = List()): List[String] =
  val cfg =
    if config.length == 0 then rec_readConfig()
    else config
  val f_crop = rec_getCrop(cfg)
  val f_scale = rec_getScale(cfg)
  val f_norm = rec_getNorm(cfg)

  val c_args =
    if f_crop.length == 2 then
      tanukifilter_crop(f_crop(0).toInt, f_crop(1).toInt)
    else List[String]()
  val s_args =
    if f_scale.length == 2 then
      tanukifilter_scale(f_scale(0).toInt, f_scale(1).toInt)
    else List[String]()
  val n_args =
    if f_norm == "true" || f_norm == "yes" then
      tanukifilter_normalize()
    else List[String]()

  c_args ++ s_args ++ n_args

def rec_getHWAccel(config: Seq[String] = List()): String =
    val cfg =
      if config.length == 0 then rec_readConfig()
      else config
    val vcodec = rec_getvcodec(cfg)
    if vcodec(0) == "qsv" then "qsv" else ""

def rec_getOutputArg(config: Seq[String] = List()): String =
  val cfg =
    if config.length == 0 then rec_readConfig()
    else config
  rec_getOutput(cfg)

def rec_getDelayArg(config: Seq[String] = List()): Int =
  val cfg =
    if config.length == 0 then rec_readConfig()
    else config
  rec_getDelay(cfg)
  
def rec_getSafeFPS(config: Seq[String] = List()): List[String] = 
  val cfg =
    if config.length == 0 then rec_readConfig()
    else config
  val vcapture = rec_getvcapture(cfg)
  if vcapture(0) == "x11grab" && vcapture(4) == "true" then
    setFramerate(vcapture(3).toInt)
  else
    List()
