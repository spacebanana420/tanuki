package tanuki.runner

import tanuki.{ffmpeg_path, ffplay_path, ffmpeg_installed}
import tanuki.config.*
import bananatui.*
import ffscala.*

import tanuki.Platform

import java.io.File
import scala.sys.process.*

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

def screenshot_convert(name: String, path: String, skip_duplicates: Boolean) =
  val newname = changeExtension(name, name.length-1)
  if !skip_duplicates || !File(s"$path/PNG/$newname").isFile() then
    encode(s"$path/$name", s"$path/PNG/$newname", args=png_setPred("mixed"), exec=ffmpeg_path)
  else println("Skipping $name, cropped screenshot already exists!")
  
def screenshot_crop(name: String, path: String, x: Int, y: Int, w: Int, h: Int, skip_duplicates: Boolean) =
  if !File(s"$path/crop").isDirectory() then File(s"$path/crop").mkdir()
  val fcrop = crop(x, y, w, h)
  val newname = changeExtension(name, name.length-1)
  if !skip_duplicates || !File(s"$path/crop/$newname").isFile() then
    encode(s"$path/$name", s"$path/crop/$newname", args=png_setPred("mixed"), filters=fcrop, exec=ffmpeg_path)
  else println(s"Skipping $name, cropped screenshot already exists!")
