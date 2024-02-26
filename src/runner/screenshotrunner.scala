package tanuki.runner

import tanuki.{ffmpeg_path, ffplay_path}
import tanuki.config.*
import java.io.File
import scala.sys.process.*

import ffscala.*

//temporary solution
def screenshot_view(path: String) =
  val cmdexec = Seq(ffplay_path, "-loglevel", "quiet", path)
  val parentpath = File(path).getParent()
  Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
  //execplay(path)

private def changeExtension(name: String, i: Int, s: String = "", copy: Boolean = false): String =
  def reverse(s: String, i: Int, ns: String = ""): String =
    if i < 0 then
      ns
    else
      reverse(s, i-1, ns + s(i))

  if i < 0 then
    reverse(s, s.length-1) + ".png"
  else if name(i) == '.' then
    changeExtension(name, i-1, s, true)
  else if copy then
    changeExtension(name, i-1, s + name(i), copy)
  else
    changeExtension(name, i-1, s, copy)

def screenshot_convert(name: String, path: String) =
  val newname = changeExtension(name, name.length-1)
  encode(s"$path/$name", s"$path/PNG/$newname", exec=ffmpeg_path)

def screenshot_crop(name: String, path: String, x: Int, y: Int, w: Int, h: Int) =
  if !File(s"$path/crop").isDirectory() then
    File(s"$path/crop").mkdir()
  val fcrop = crop(x, y, w, h)
  val newname = changeExtension(name, name.length-1)
  encode(s"$path/$name", s"$path/crop/$newname", filters=fcrop, exec=ffmpeg_path)
