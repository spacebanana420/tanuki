package tanuki.runner

import tanuki.config.*
import tanuki.tui.*
import tanuki.recorder.*

import java.io.File
import scala.sys.process.*
import ffscala.capture.*

private def getVideoName(path: String, name: String = "tanuki-video.mov", i: Int = 1): String =
  if !File(s"$path/$name").isFile() then
    name
  else
    getVideoName(path, s"tanuki-video-$i.mov", i+1)

def launchGame(path: String, recordvideo: Boolean = false, reccfg: Seq[String] = List()) =
  val cfg = readConfig()
  val cmd = getCommand(cfg)
  val cmd_start = getStartCmd(cfg)
  val cmd_close = getCloseCmd(cfg)
  val parentpath = File(path).getParent()

  val cmdexec =
    if cmd == "" then
      Seq(path)
    else if steamRunEnabled(cfg) then
      Seq("steam-run", cmd, path)
    else
      Seq(cmd, path)
  if cmd_start.length != 0 then
    cmd_start.run(ProcessLogger(line => ()))
  val game = Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
  if recordvideo then
    recordGameplay(reccfg)
  else
    Thread.sleep(3000)
    readUserInput("\nPress enter to return to the main menu")
  if cmd_close.length != 0 then
    cmd_close.run(ProcessLogger(line => ()))
  game.destroy()


private def recordGameplay(cfg: List[String]) =
  val captureargs = rec_getCaptureArgs(cfg)
  val args = rec_getEncodeArgs(cfg)
  val filters = rec_getFilterArgs(cfg)

  val output = rec_getOutput(cfg)
  val d = rec_getDelay(cfg)
  val delay = if d > 60 then 60 else d
  val name = getVideoName(output)

  if delay > 0 then
    println(s"Recording will begin in $delay seconds")
    Thread.sleep(delay*1000)
  println("\nPress Q to stop recording and return to the main menu")
  record(s"$output/$name", captureargs, args, filters, rec_getHWAccel(cfg))