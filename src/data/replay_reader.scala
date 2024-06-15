package tanuki.data

import java.io.{FileInputStream, FileOutputStream}
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

object threplay:
  private def isVersionHeader(replay: Array[Byte], keyword: String, i: Int, txt: String = ""): Boolean =
    if txt == keyword then true
    else if txt.length == keyword.length || i >= replay.length then false
    else isVersionHeader(replay, keyword, i+1, txt + replay(i).toChar)

  private def findHeaderLocation(replay: Array[Byte], keyword: String, i: Int = 0): Int =
    if i >= replay.length then -1
    else if replay(i).toChar == keyword(0) && isVersionHeader(replay, keyword, i) then i
    else findHeaderLocation(replay, keyword, i+1)

  private def filterReplay(replay: Array[Byte], i: Int, newReplay: Array[Byte] = Array()): Array[Byte] =
    if i >= replay.length then newReplay
    else filterReplay(replay, i+1, newReplay :+ replay(i))

  private def getValue(replay: Array[Byte], i: Int, length: Int, str: String = "", strlen: Int = 0): String =
    if i >= replay.length || strlen >= length then str
    else
      val c = replay(i).toChar
      getValue(replay, i+1, length, str + c, strlen + 1)

  def openReplay(path: String): Array[Byte] =
    val replay = FileInputStream(path).readAllBytes()
    val i = findHeaderLocation(replay, "Version")
    if i != -1 then
      filterReplay(replay, i)
    else Array()

  def openReplay_raw(path: String): Array[Byte] =
    FileInputStream(path).readAllBytes()

  def getReplayName(replay: Array[Byte]): String =
    val i = findHeaderLocation(replay, "Name ")
    getValue(replay, i + "Name ".length, 8)

  def getReplayCharacter(replay: Array[Byte]): String =
    val i = findHeaderLocation(replay, "Chara ")
    getValue(replay, i + "Chara ".length, 7)

  def getReplayRank(replay: Array[Byte]): String =
    val i = findHeaderLocation(replay, "Rank ")
    getValue(replay, i + "Rank ".length, 7)

  def getReplayDate(replay: Array[Byte]): String =
    val i = findHeaderLocation(replay, "Date ")
    getValue(replay, i + "Date ".length, 14)

  def getReplayProperties(replay: Array[Byte]): Vector[String] =
    var name = ""; var chara = ""; var rank = ""; var date = "";
    var name_done = false; var chara_done = false; var rank_done = false; var date_done = false;

    Future{name = getReplayName(replay); name_done = true}
    Future{chara = getReplayCharacter(replay); chara_done = true}
    Future{rank = getReplayRank(replay); rank_done = true}
    Future{date = getReplayDate(replay); date_done = true}

    while !name_done || !chara_done || !rank_done || !date_done do Thread.sleep(1)
    Vector(name, chara, rank, date)



