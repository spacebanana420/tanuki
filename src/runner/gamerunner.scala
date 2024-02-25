package tanuki.runner

import tanuki.{ffmpeg_installed, recording_supported}
import tanuki.config.*
import tanuki.tui.*
import tanuki.recorder.*

import java.io.File
import scala.sys.process.*
import ffscala.capture.*

private def getVideoName(path: String, name: String = "tanuki-video", i: Int = 1): String =
  if !File(s"$path/$name").isFile() then
    name + ".mov"
  else
    getVideoName(path, s"$name-$i", i+1)

def launchGame(path: String, name: String, recordvideo: Boolean = false, reccfg: Seq[String] = List()) =
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
  if recordvideo then recordGameplay(reccfg, name)
  else Thread.sleep(3000)
  readUserInput("\nPress enter to return to the main menu")
    // if standbyInput() then
    //   recordGameplay(gamename = name, usedelay = false)
  if cmd_close.length != 0 then
    cmd_close.run(ProcessLogger(line => ()))
  game.destroy()

// private def standbyInput(): Boolean =
//   val record_ready =
//     ffmpeg_installed
//     && recording_supported
//     && rec_configExists()
//     && rec_isConfigOk()

//   val ans =
//     if record_ready then
//       readUserInput("\nPress enter to return to the main menu\nOr press R + enter to start recording")
//     else readUserInput("\nPress enter to return to the main menu")

//   if !record_ready then
//     false
//   else if ans == "r" || ans == "R" then
//     true
//   else
//     false
  
def recordGameplay(cfg: Seq[String] = List(), gamename: String = "", waitconfirm: Boolean = true) =
  val captureargs = rec_getCaptureArgs(cfg)
  val args = rec_getEncodeArgs(cfg) ++ rec_getSafeFPS(cfg)
  val filters = rec_getFilterArgs(cfg)

  val output = rec_getOutputArg(cfg)
  val d = rec_getDelayArg(cfg)
  val delay =
    if d > 60 then 60
    else d
  val name =
    if gamename == "" then
      getVideoName(output, s"tanuki-video")
    else
      getVideoName(output, s"tanuki-video-$gamename")
  if waitconfirm then readUserInput("Press enter to begin recording")
  if delay > 0 then
    println(s"Recording will begin in $delay seconds")
    Thread.sleep(delay*1000)
  println("\nPress Q to stop recording")
  record(s"$output/$name", captureargs, args, filters, rec_getHWAccel(cfg))