package ffscala.capture

import ffscala.*
import scala.sys.process._
import ffscala.misc.*
import java.io.File

private def parse(sources: String, s: String = "", l: List[String] = List(), i: Int = 0, copy: Boolean = false): List[String] =
  if i >= sources.length then
    if s != "" then
      l :+ s
    else
      l
  else if sources(i) == '\n' then
    if s != "" then parse(sources, "", l :+ s, i+1, true) else parse(sources, "", l, i+1, true)
  else if sources(i) == '[' then
    parse(sources, s, l, i+1, false)
  else if sources(i) != ' ' && sources(i) != '*' && copy then
    parse(sources, s + sources(i), l, i+1, copy)
  else
    parse(sources, s, l, i+1, copy)

private def parse_dshow(sources: String, video: Boolean): List[String] =
  def getLine(src: String, s: String = "", i: Int = 0, copy: Boolean = false): String =
    if i >= src.length then
      s
    else if src(i) == '"' then
      getLine(src, s, i+1, !copy)
    else if copy then
      getLine(src, s + src(i), i+1, copy)
    else
      getLine(src, s, i+1, copy)

  if video then
    mkList(sources).filter(x => x.contains("(video)")).map(x => getLine(x))
  else
    mkList(sources).filter(x => x.contains("(audio)")).map(x => getLine(x))
  

private def mkList(sources: String, s: String = "", l: List[String] = List(), i: Int = 0): List[String] =
  if i >= sources.length then
    if s != "" then
      l :+ s
    else
      l
  else if sources(i) == '\n' then
    mkList(sources, "", l :+ s, i+1)
  else
    mkList(sources, s + sources(i), l, i+1)


//Remove unwanted list elements
// private def filterSources(s: List[String], f: List[String] = List(), i: Int = 0): List[String] =
//   def filter(source: String, filtered: String = "", c: Int = 0): String =
//     if c >= source.length || source(c) == ' ' then
//       filtered
//     else
//       filter(source, filtered + source(c), i+1)
//
//   if i >= s.length then
//     f
//   else
//     filterSources(s, f :+ filter(s(i)), i+1)

def listSources(mode: String, full: Boolean = false, exec: String = "ffmpeg"): List[String] =
  val supported = supportedCaptureModes()

  if mode == "all" then
    val cmd = List(exec, "-hide_banner", "-sources")
    parse(cmd.!!)
  else if belongsToList(mode, supported) then
    val cmd = List(exec, "-hide_banner") ++ getSourcesArgs(mode)
    val sources = cmd.!!
    if full || mode == "avfoundation" then
      mkList(sources)
    else if mode == "dshow_video" then
      parse_dshow(sources, true)
    else if mode == "dshow_audio" then
      parse_dshow(sources, false)
    else 
      parse(sources)
  else
    List()

private def getSourcesArgs(mode: String): List[String] =
  mode match
    case "dshow" =>
      List("-list_devices", "true", "-f", "dshow", "-i", "dummy")
    case "avfoundation" =>
      List("-f", "avfoundation", "-list_devices", "true", "-i", "\"\"")
    case _ =>
      List("-sources", mode)

def listSources_OSS(): Array[String] = //alternative source listing for OSS
  if File("/dev").isDirectory() then
    File("/dev")
      .list()
      .filter(x => x.contains("dsp"))
      .map(x => s"/dev/$x")
  else
    Array()

// def listDirectSources(exec: String = "ffmpeg"): List[String] =
//   List(exec, "-list_devices", "true", "-f", "dshow", "-i", "dummy").!!
