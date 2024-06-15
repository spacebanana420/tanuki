package tanuki.data

import bananatui.*
import tanuki.tui.tui_chooseDataDir
import java.io.File


private def mkOption(name: String, path: String): String =
  val replay = threplay.openReplay(path)
  val info = threplay.getReplayProperties(replay)
  s"File: $name\n\tName: ${info(0)}\n\tCharacter: ${info(1)}\n\tRank: ${info(2)}\n\tDate: ${info(3)}\n"

def listReplays(): String =
  val datadir = tui_chooseDataDir()
  val replay_dirs = File(datadir).list().filter(x => x.contains("replay"))
  val rdir =
    if replay_dirs.length == 0 then
      pressToContinue("No replay files have been found!")
      ""
    else if replay_dirs.length == 1 then replay_dirs(0)
    else
      val title = "The following replay directories were found\nChoose one"
      chooseOption_astring(replay_dirs, title, "Cancel")
  if rdir != "" then
    val replays =
      File(s"$datadir/$rdir").list()
      .filter(x => x.contains(".rpy"))
      .map(x => mkOption(x, s"$datadir/$rdir/$x"))
    val chosenreplay = chooseOption_array(replays, "The following replays have been found")
    s"$datadir/$rdir/$chosenreplay"
  else ""
