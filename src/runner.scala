package tanuki.runner

import tanuki.config.*
import java.io.File
import scala.sys.process.*


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

  Process(cmdexec, File(parentpath)).!(ProcessLogger(line => ()))


//temporary solution
def screenshot_view(path: String) =
  val cmdexec = Seq("ffplay", path)
  val parentpath = File(path).getParent()
  Process(cmdexec, File(parentpath)).run(ProcessLogger(line => ()))
