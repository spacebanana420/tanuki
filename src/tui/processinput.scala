package tanuki.tui

import java.io.File

def answerToNumber(str: String): Int =
  try
    str.toInt
  catch
    case e: Exception => -1

def answerToShort(str: String): Short =
  try
    str.toShort
  catch
    case e: Exception => -1

def answerToByte(str: String): Byte =
  try
    str.toByte
  catch
    case e: Exception => -1

def getList(l: List[String], txt: String = s"Choose an entry\n\n${green}${0}:${default} Exit\n\n", i: Int = 0): String =
  if i >= l.length then
    txt
  else
    val line = s"${green}${i+1}:${default} ${l(i)}\n"
    getList(l, txt + line, i+1)

def getArray(l: Array[String], txt: String = s"Choose an entry\n\n${green}${0}:${default} Exit\n\n", i: Int = 0): String =
  if i >= l.length then
    txt
  else
    val line = s"${green}${i+1}:${default} ${l(i)}\n"
    getArray(l, txt + line, i+1)

def readLoop(txt: String, maxval: Int): Int =
  val answer = answerToNumber(spawnAndRead(txt))
  if answer == 0 || (1 to maxval).contains(answer) then
    answer
  else
    readLoop(txt, maxval)

def readLoop_list(l: List[String], title: String = s"Choose an entry\n\n${green}${0}:${default} Exit\n\n"): Int =
  val txt_list = getList(l, title)
  readLoop(txt_list, l.length)

def readLoop_array(l: Array[String], title: String = s"Choose an entry\n\n${green}${0}:${default} Exit\n\n"): Int =
  val txt_list = getArray(l, title)
  readLoop(txt_list, l.length)

def readLoop_int(txt: String): Int =
  val answer = answerToNumber(spawnAndRead(txt))
  if answer != -1 then
    answer
  else
    readLoop_int(txt)

def readLoop_byte(txt: String): Byte =
  val answer = answerToByte(spawnAndRead(txt))
  if answer != -1 then
    answer
  else
    readLoop_byte(txt)

def readLoop_short(txt: String): Short =
  val answer = answerToShort(spawnAndRead(txt))
  if answer != -1 then
    answer
  else
    readLoop_short(txt)

def readLoop_dir(txt: String): String =
  val answer = spawnAndRead(txt)
  if File(answer).isDirectory() then
    answer
  else if answer == "" then
    "."
  else
    pressToContinue("That is not a real path in your system!")
    readLoop_dir(txt)