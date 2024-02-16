package tanuki.recorder

import tanuki.misc.similarInList

import java.io.File

def rec_isConfigOk(config: List[String] = List()): Boolean =
  val cfg =
    if config == List() then
      rec_readConfig()
    else
      config
  if output_ok(cfg) && vcodec_ok(cfg) && acodec_ok(cfg) && vcapture_ok(cfg) && acapture_ok(cfg) then
    true
  else
    false

private def output_ok(cfg: List[String]): Boolean =
  val output = rec_getOutput(cfg)
  File(output).isDirectory()

private def vcodec_ok(cfg: List[String]): Boolean =
  val vcodec = rec_getvcodec(cfg)
  try
    vcodec(0) match
      case "x264" =>
        vcodec(2).toByte;
        if vcodec.length == 4 then true else false
      case "x264rgb" =>
        vcodec(2).toByte;
        if vcodec.length == 3 then true else false
      case _ => false
  catch
    case e: Exception => false

private def acodec_ok(cfg: List[String]): Boolean =
  val acodec = rec_getacodec(cfg)
  try
    acodec(0) match
      case "pcm" =>
        acodec(1).toByte
        if acodec.length == 2 then true else false
      case "opus" =>
        acodec(1).toInt
        if acodec.length == 2 then true else false
      case "mp3" =>
        acodec(1).toInt
        if acodec.length == 2 then true else false
      case _ => false
  catch case e: Exception => false

private def vcapture_ok(cfg: List[String]): Boolean =
  val vcapture = rec_getvcapture(cfg)
  try
    vcapture(0) match
      case "x11grab" =>
        vcapture(1).toInt; vcapture(2).toInt; vcapture(3).toInt
        if vcapture.length == 4 then true else false
      case _ => false
  catch
    case e: Exception => false

private def acapture_ok(cfg: List[String]): Boolean =
  val acapture = rec_getacapture(cfg)
  try
    acapture(0) match
      case "pulse" =>
        if acapture.length == 2 then true else false
      case _ => false
  catch
    case e: Exception => false
