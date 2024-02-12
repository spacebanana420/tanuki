package tanuki.runner

import tanuki.config.*
import tanuki.tui.*
import java.io.File
import scala.sys.process.*


def launchGame(path: String) =
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
  Thread.sleep(4000)
  readUserInput("\nPress enter to return to the main menu")
  if cmd_close != List() then
    cmd_close.run(ProcessLogger(line => ()))
  game.destroy()
