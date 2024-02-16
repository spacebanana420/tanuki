package tanuki.runner

import tanuki.config.*
import tanuki.tui.*
import tanuki.recorder.*

import java.io.File
import scala.sys.process.*
import ffscala.capture.*

def launchGame(path: String, recordvideo: Boolean = false, reccfg: List[String] = List()) =
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
  if cmd_start != List() then
    cmd_start.run(ProcessLogger(line => ()))
  val game = Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
  if recordvideo then
    val captureargs = rec_getCaptureArgs(reccfg)
    val args = rec_getEncodeArgs(reccfg)
    val output = rec_getOutput(reccfg)
    val d = rec_getDelay(reccfg)
    val delay = if d > 60 then 60 else d

    if delay > 0 then Thread.sleep(delay*1000)
    println("\nPress Q to stop recording and return to the main menu")
    record(s"$output/tanuki-video.mov", captureargs, args)
  else
    Thread.sleep(3000)
    readUserInput("\nPress enter to return to the main menu")
  if cmd_close != List() then
    cmd_close.run(ProcessLogger(line => ()))
  game.destroy()
