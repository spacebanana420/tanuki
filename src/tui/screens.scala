package tanuki.tui

import tanuki.runner.*
import tanuki.config.*
import scala.sys.exit

val green = foreground("green")
val default = foreground("default")
val yellow = foreground("yellow")

def tui_title() =
  val text = s"$yellow[Tanuki Launcher]$default\nVersion 0.1\n\n${green}0:$default Exit\n${green}1:$default Play\n${green}2:$default Manage data\n${green}3:$default Configure launcher\n\n"
  while true do
    val answer = answerToNumber(spawnAndRead(text))
    answer match
      case 0 =>
        exit()
      case 1 =>
        tui_play()
      case 2 =>
      case 3 =>


def tui_noentries() =
  val text = "No entries have been found!\nWould you like to configure Tanuki now? (y/n)"

  val answer = spawnAndRead()


def tui_play() =
  def getList(l: List[String], txt: String = s"Choose a game\n\n${green}${0}:${default} Exit\n", i: Int = 0): String =
    if i >= l.length then
      txt
    else
      val line = s"${green}${i+1}:${default} ${l(i)}\n"
      getList(l, txt + line, i+1)

  val games = getGames(readConfig())
  val names = games.map(x => parseEntry(x)(0))
  val paths = games.map(x => parseEntry(x)(1))
  val text = getList(names)

  var done = false
  while done == false do
    val answer = answerToNumber(spawnAndRead(text))

    if answer == 0 then
      done = true
    else if (1 to names.length).contains(answer) then
      done = true
      println(s"Launching ${names(answer-1)}\nGirls are now praying, please wait warmly...")
      launchGame(paths(answer-1))


