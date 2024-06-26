package bananatui

import scala.sys.process.*
//import scala.io.StdIn.readLine

def windows_enableANSI(): Boolean =
  try
    Vector("cmd", "/c", "echo", "").!
    true
  catch case e: Exception => false

def printProgress(percentage: Byte, str: String = "", first: Boolean = true, usebar: Boolean = true) =
  def mkBar(chr: Char, amt: Byte, bar: String = "", i: Int = 0): String =
  if i >= amt then bar
  else mkBar(chr, amt, bar + chr, i+1)

  def strLines(lines: Int = 1, i: Int = 0): Int =
    if str == "" then 0
    else if i >= str.length then lines
    else if str(i) == '\n' then
      strLines(lines+1, i+1)
    else strLines(lines, i+1)

  val perc: Byte = if percentage > 100 then 100 else if percentage < 0 then 0 else percentage
  val lines = strLines()
  val extrastr = if lines == 0 then "" else str
  val progress =
    if usebar then
      s"Progress: [${mkBar('*', (perc/10).toByte)}${mkBar('-', (10-(perc/10)).toByte)}] $perc%"
    else s"Progress: $perc%"


  if first then
    println(s"${str}$progress")
  else
    val clear = s"\u001B[0K"
    val moveup = s"\u001B[${lines}A"

    println(s"${moveup}${clear}${str}$progress")

private def test() =
  printProgress(30, "Test\n\n")
  Thread.sleep(2000)
  for i <- 1 to 10 do
    printProgress(30, "Test\n\n", false)
    Thread.sleep(2000)

def searchList(items: Seq[String], keyword: String, casesensitive: Boolean = false): String =
  def keywordMatches(candidate: String): Boolean =
    (casesensitive && candidate == keyword) || (candidate.toLowerCase() == keyword.toLowerCase())


  def mklist(title: String, first: String, txt: String = "", i: Int = 0): String =
    if i >= items.length then
      s"$title\n\n${foreground("green")}0: ${foreground("default")}$first\n\n$txt"
    else if keywordMatches(items(i)) then
      val line = s"${foreground("green")}${i+1}:${foreground("default")} ${items(i)}\n"
      mklist(title, first, txt + line, i+1)
    else
      mklist(title, first, txt, i+1)


  val title = "The following entries have been found"
  val firstval = "Return to list"
  mklist(title, firstval)

def searchList_read(items: Seq[String], keyword: String, casesensitive: Boolean = false): Int =
  val opts = searchList(items, keyword, casesensitive)
  readLoop(opts, items.length)

