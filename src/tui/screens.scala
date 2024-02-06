package tanuki.tui

val green = foreground("green")
val default = foreground("default")
val yellow = foreground("yellow")

def tui_title(): Int =
  val text = s"$yellow[Tanuki Launcher]$default\nVersion 0.1\n\n${green}1:$default Play\n${green}2:$default Manage data\n${green}3:$default Configure launcher\n\n"

  val answer = spawnAndRead(text)
  answerToNumber(answer)
