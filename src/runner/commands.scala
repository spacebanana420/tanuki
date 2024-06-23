package tanuki.runner

import scala.sys.process.*

import tanuki.tui.*
import tanuki.config.*
import bananatui.*

private def getcommand(line: String, name: String): List[String] =
    parseCommand(line, i = name.length+1) //+1 to skip : character

def launchCommand(title: String) =
  val entries = getGames_cmd(readConfig())
  if !tui_noentries(entries) then
    val names = entries.map(x => gamecmd_getname(x))

    val answer = chooseOption(names, s"$title\n\nChoose a command to run")
    if answer != 0 then
      try
        val cmd = getcommand(entries(answer-1), names(answer-1))
        cmd.run(ProcessLogger(line => ()))
      catch case e: Exception => pressToContinue("There was an error running the command!")
