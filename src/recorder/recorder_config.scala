package tanuki.recorder

import tanuki.misc.similarInList
import java.io.File
import java.io.FileOutputStream
import scala.io.Source

def rec_createConfig() = FileOutputStream("video_config.txt")

def rec_readConfig(): List[String] =
  val settings =
    List(
    "width=", "height=", "vcodec=", "acodec=",
    "vcapture=", "acapture="
    )
  val src = Source.fromFile("video_config.txt")
  val cfg = src
    .getLines()
    .filter(x => x.length > 0 && similarInList(x, settings) && x(0) != '#')
    .toList
  src.close()
  cfg

def rec_readEntry(cfg: List[String], setting: String): List[String] =
  def startsWith(line: String, tmp: String = "", i: Int = 0): Boolean =
    if i >= line.length || i >= setting.length then
      if tmp == setting then
        true
      else
        false
    else
      startsWith(line, tmp + line(i), i+1)

  def find(i: Int = 0): Int =
    if i >= cfg.length then
      -1
    else if startsWith(cfg(i)) then
      i
    else
      find(i+1)

  def parse(setting: String, tmp: String = "", nl: List[String] = List(), i: Int = 0): List[String] =
    if i >= setting.length then
      if tmp == "" then
        nl
      else
        nl :+ tmp
    else if setting(i) == ':' then
      parse(setting, "", nl :+ tmp, i+1)
    else
      parse(setting, tmp + setting(i), nl, i+1)

  val line = find()
  if line == -1 then
    List()
  else
    parse(cfg(line))

def rec_getvcodec(cfg: List[String]): List[String] = rec_readEntry(cfg, "vcodec=")
def rec_getacodec(cfg: List[String]): List[String] = rec_readEntry(cfg, "acodec=")
def rec_getvcapture(cfg: List[String]): List[String] = rec_readEntry(cfg, "vcapture=")
def rec_getacapture(cfg: List[String]): List[String] = rec_readEntry(cfg, "acapture=")

def getFullArgs(): List[String] =
  val cfg = rec_readConfig()
  val vcodec = rec_getvcodec(cfg)
  val acodec = rec_getacodec(cfg)
  val vcapture = rec_getvcapture(cfg)
  val acapture = rec_getacapture(cfg)

  val v_args =
    vcodec(0) match
    case "x264" =>
      video_setx264(vcodec(1), vcodec(2).toByte, vcodec(3).toShort, vcodec(4))
    case _ => List[String]()
  val a_args =
    acodec(0) match
      case "pcm" =>
        audio_setPCM(acodec(1).toByte)
      case "opus" =>
        audio_setOpus(acodec(1).toInt)
      case _ => List[String]()

  val vcapture_args = capture_x11(vcapture(1).toInt, vcapture(2).toInt, vcapture(3).toInt)
  val acapture_args = capture_pulse(acapture(1))

  v_args ::: a_args ::: vcapture_args ::: acapture_args


def rec_writeConfig(
vcodec: List[String] = List(), acodec: List[String] = List(), vcapture: List[String] = List(), acapture: List[String] = List()
) =
  def mkstring(l: List[String], s: String = "", i: Int = 0): String =
    if i >= l.length then
      s + "\n"
    else
      mkstring(l, s"$s:${l(i)}", i+1)

  val config =
    "vcodec=" + mkstring(vcodec)
    + "acodec=" + mkstring(acodec)
    + "vcapture=" + mkstring(vcapture)
    + "acapture=" + mkstring(acapture)

  FileOutputStream("video_config.txt").write(config.getBytes())
