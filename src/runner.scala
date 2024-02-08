package tanuki.runner

import tanuki.config.*
import tanuki.tui.*
import java.io.File
import scala.sys.process.*

import ffscala.*


def launchGame(path: String) =
  val cfg = readConfig()
  val cmd = getCommand(cfg)
  val parentpath = File(path).getParent()

  val cmdexec =
    if cmd == "" then
      Seq(path)
    else if steamRunEnabled(cfg) then
      Seq("steam-run", cmd, path)
    else
      Seq(cmd, path)

  val game = Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
  Thread.sleep(4000)
  readUserInput("\nPress enter to return to the main menu")
  game.destroy()

//temporary solution
def screenshot_view(path: String) =
  val cmdexec = Seq("ffplay", path)
  val parentpath = File(path).getParent()
  Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))


def screenshot_crop(path: String, image: String, x: Int, y: Int, w: Int, h: Int) =
  if !File(s"$path/crop").isDirectory() then
    File(s"$path/crop").mkdir()
  val fcrop = crop(x, y, w, h)
  encode(s"$path/$image", s"$path/crop/$image", filters=fcrop)
