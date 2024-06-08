package tanuki.runner

import tanuki.{ffmpeg_path, ffplay_path, recording_supported, system_platform}
import tanuki.config.*
import tanuki.tui.*
import tanuki.recorder.*

import bananatui.*
import java.io.File
import scala.sys.process.*
import ffscala.capture.*

private def getVideoName(path: String, name: String = "tanuki-video", i: Int = 0): String =
  if i == 0 && !File(s"$path/$name.mov").isFile() then
    name + ".mov"
  else if i != 0 && !File(s"$path/$name-$i.mov").isFile() then
    s"$name-$i.mov"
  else
    getVideoName(path, name, i+1)

private def isProgramNative(filename: String): Boolean =
  val windows_related = filename.contains(".exe") || filename.contains(".lnk") || filename.contains(".msi")
  if system_platform == 0 || !windows_related then true else false

def groupWineEnvs(env_wine: String, env_dxvk: String, group: Vector[(String, String)] = Vector(), i: Int = 0): Vector[(String, String)] = //maybe replace with pure if statements
    i match
      case 0 =>
        if env_wine != "" then
          groupWineEnvs(env_wine, env_dxvk, group :+ ("WINEPREFIX", env_wine), i+1)
        else
          groupWineEnvs(env_wine, env_dxvk, group, i+1)
      case 1 =>
        if env_dxvk != "" then
          groupWineEnvs(env_wine, env_dxvk, group :+ ("DXVK_FRAME_RATE", env_dxvk), i+1)
        else
          groupWineEnvs(env_wine, env_dxvk, group, i+1)
      case _ =>
        group

def launchGame(path: String, name: String, recordvideo: Boolean = false, reccfg: Seq[String] = List()) =
  val cfg = readConfig()

  lazy val cmd = getCommand(cfg)
  lazy val wine = getWinePath(cfg)

  val cmd_start = getStartCmd(cfg)
  val cmd_close = getCloseCmd(cfg)
  val close_on_return = getReturnClose(cfg)

  val wine_prefix = getWinePrefix(cfg)
  val dxvk_fps = getDxvkFramerate(cfg)
  lazy val wine_envs = groupWineEnvs(wine_prefix, dxvk_fps)

  val is_program_native = isProgramNative(File(path).getName())
  val runner = if is_program_native then cmd else wine
  val parentpath = File(path).getParent()

  val cmdexec =
    if runner == "" then
      Seq(path)
    else if steamRunEnabled(cfg) then
      Seq("steam-run", runner, path)
    else
      Seq(runner, path)
  if cmd_start.length != 0 then
    cmd_start.run(ProcessLogger(line => ()))
  val game =
    if system_platform != 0 && cmd != "" then
      wine_envs.length match
        case 1 =>
          Process(cmdexec, File(parentpath), wine_envs(0)).run(ProcessLogger(line => ()))
        case 2 =>
          Process(cmdexec, File(parentpath), wine_envs(0), wine_envs(1)).run(ProcessLogger(line => ()))
        case _ =>
          Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
    else
      Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))

  if recordvideo then
    recordGameplay(reccfg, name)
  else
    Thread.sleep(3000)
  readUserInput("\nPress enter to return to the main menu")
  if cmd_close.length != 0 then
    cmd_close.run(ProcessLogger(line => ()))
  if close_on_return then game.destroy()

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
  record(s"$output/$name", captureargs, args, filters, rec_getHWAccel(cfg), exec=ffmpeg_path)
