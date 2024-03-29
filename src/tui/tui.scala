package tanuki.tui

import java.io.File
import scala.sys.process.*
import scala.io.StdIn.readLine

//ANSI escape codes, that's where the magic strings come from

def readUserInput(message: String = ""): String =
  if message != "" then
    println(message)
  readLine()

def spawnAndRead(message: String): String = readUserInput(s"\u001B[3J\u001B[1J\u001B[H$message")

def pressToContinue(message: String = ""): String = readUserInput(message + "\n\nPress enter to continue")

// def clear() = { //test windows support
//   if File("C:").isDirectory == false then
//     List[String]("clear").!
//   else
//     List[String]("cmd", "/c", "cls").!
// }

def spawnScreen(ui: String) =
  print(s"\u001B[3J\u001B[1J\u001B[H$ui")

def askPrompt(ui: String, clear: Boolean = true): Boolean =
//   val yellow = foreground("yellow")
//   val default = foreground("default")
  val answer =
    if clear then
      spawnAndRead(s"$ui ${yellow}(y/n)$default\n").toLowerCase
    else
      readUserInput(s"$ui ${yellow}(y/n)$default\n").toLowerCase
  if answer == "yes" || answer == "y" then
    true
  else
    false

def clear() = print("\u001B[3J\u001B[1J\u001B[H")

def saveScreen() = print("\u001B[?47h")

def restoreScreen() = print("\u001B[?47l")

def moveCursor(mode: String, lines: Int) =
  if mode == "up" then
    print(s"\u001B[${lines}A")
  else
    print(s"\u001B[${lines}B")

def clearBelowCursor(lines: Int) = print(s"\u001B[${lines}A\u001B[0K")

def printStatus(msg: String, isError: Boolean = true) =
  val default = foreground("default")
  if isError then
    val red = foreground("red")
    println(s"[${red}Error$default] $msg")
  else
    val yellow = foreground("yellow")
    println(s"[${yellow}Warning$default] $msg")


def foreground(color: String = "default"): String =
  color match
    case "black" => "\u001B[30m"
    case "red" => "\u001B[31m"
    case "green" => "\u001B[32m"
    case "yellow" => "\u001B[33m"
    case "blue" => "\u001B[34m"
    case "magenta" => "\u001B[35m"
    case "cyan" => "\u001B[36m"
    case "white" => "\u001B[37m"
    case "default" => "\u001B[39m"
    case "reset" => "\u001B[0m"
    case _ => "\u001B[39m"

def background(color: String = "default"): String =
  color match
    case "black" => "\u001B[40m"
    case "red" => "\u001B[41m"
    case "green" => "\u001B[42m"
    case "yellow" => "\u001B[43m"
    case "blue" => "\u001B[44m"
    case "magenta" => "\u001B[45m"
    case "cyan" => "\u001B[46m"
    case "white" => "\u001B[47m"
    case "default" => "\u001B[49m"
    case "reset" => "\u001B[0m"
    case _ => "\u001B[49m"
