package tanuki.recorder

import tanuki.misc.similarInList

import java.io.File
import java.io.FileOutputStream
import scala.io.Source

def rec_createConfig() = FileOutputStream("video_config.txt")

def rec_configExists(): Boolean = File("video_config.txt").isFile()

def rec_readConfig(): Vector[String] =
  val settings =
    List(
    "output=", "delay=",
    "width=", "height=", "vcodec=", "acodec=",
    "vcapture=", "acapture="
    )
  val src = Source.fromFile("video_config.txt")
  val cfg = src
    .getLines()
    .filter(x => x.length > 0 && similarInList(x, settings) && x(0) != '#')
    .toVector
  src.close()
  cfg

private def find(cfg: Seq[String], setting: String, i: Int = 0): Int =
  def startsWith(line: String, tmp: String = "", i: Int = 0): Boolean =
    if i >= line.length || i >= setting.length then
      if tmp == setting then
        true
      else
        false
    else
      startsWith(line, tmp + line(i), i+1)

  if i >= cfg.length then
    -1
  else if startsWith(cfg(i)) then
    i
  else
    find(cfg, setting, i+1)

private def rec_readEntry(cfg: Seq[String], setting: String): List[String] =
  def getValue(line: String, tmp: String = "", nl: List[String] = List(), i: Int = 0): List[String] =
    if i >= line.length then
      if tmp == "" then
        nl
      else
        nl :+ tmp
    else if line(i) == ':' then
      getValue(line, "", nl :+ tmp, i+1)
    else
      getValue(line, tmp + line(i), nl, i+1)

  val i = find(cfg, setting)
  if i == -1 then
    List()
  else
    getValue(cfg(i), i = setting.length)

private def rec_readSingleValue(cfg: Seq[String], setting: String): String =
  def getValue(line: String, tmp: String = "", i: Int): String =
    if i >= line.length then
      tmp
    else
      getValue(line, tmp + line(i), i+1)

  val i = find(cfg, setting)
  if i == -1 then
    ""
  else
    getValue(cfg(i), i = setting.length)

private def rec_getvcodec(cfg: Seq[String]): List[String] = rec_readEntry(cfg, "vcodec=")
private def rec_getacodec(cfg: Seq[String]): List[String] = rec_readEntry(cfg, "acodec=")
private def rec_getvcapture(cfg: Seq[String]): List[String] = rec_readEntry(cfg, "vcapture=")
private def rec_getacapture(cfg: Seq[String]): List[String] = rec_readEntry(cfg, "acapture=")

private def rec_getCrop(cfg: Seq[String]): List[String] = rec_readEntry(cfg, "crop=")
private def rec_getScale(cfg: Seq[String]): List[String] = rec_readEntry(cfg, "scale=")

def rec_getOutput(cfg: Seq[String]): String = rec_readSingleValue(cfg, "output=")
def rec_getDelay(cfg: Seq[String]): Int = rec_readSingleValue(cfg, "delay=").toInt

def rec_writeConfig(
output: String, delay: Int,
vcodec: Seq[String] = List(), acodec: Seq[String] = List(), vcapture: Seq[String] = List(), acapture: Seq[String] = List()
) =
  def mkstring(l: Seq[String], s: String = "", i: Int = 0): String =
    if i >= l.length then
      s + "\n"
    else if i == 0 then
      mkstring(l, s"${l(i)}", i+1)
    else
      mkstring(l, s"$s:${l(i)}", i+1)

  val config =
    s"output=$output\ndelay=$delay\n"
    + "vcodec=" + mkstring(vcodec)
    + "acodec=" + mkstring(acodec)
    + "vcapture=" + mkstring(vcapture)
    + "acapture=" + mkstring(acapture)

  FileOutputStream("video_config.txt").write(config.getBytes())
