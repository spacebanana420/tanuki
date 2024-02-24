package tanuki.recorder

import tanuki.misc.similarInList

import java.io.File

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
      case "pulse" =>
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
