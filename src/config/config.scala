package tanuki.config

import java.io.File
import scala.io.Source


def configExists(): Boolean = File("config.txt").exists()

def readConfig(): List[String] =
  Source.fromFile("config.txt")
    .getLines().toList
    .filter(x => x.length > 0 && (x.contains("game=") || x.contains("data=") || x.contains("command=")) && x(0) != '#')

def getValue(l: String, setting: String, tmp: String = "", value: String = "", i: Int = 0): String =
  if i >= l.length || (i >= setting.length && setting != tmp) then
    value
  else if tmp == setting then
    getValue(l, setting, tmp, value + l(i), i+1)
  else
    getValue(l, setting, tmp + l(i), value, i+1)

def getValues(cfg: List[String], setting: String, vals: List[String] = List(), i: Int = 0): List[String] =
  if i >= cfg.length then
    vals
  else
    val value = getValue(cfg(i), setting)
    if value != "" then
      getValues(cfg, setting, vals :+ value, i+1)
    else
      getValues(cfg, setting, vals, i+1)



def getGames(cfg: List[String]): List[String] = getValues(cfg, "game=")
def getDatas(cfg: List[String]): List[String] = getValues(cfg, "data=")
def getCommand(cfg: List[String], i: Int = 0): String =
  if i >= cfg.length then
    ""
  else
    val value = getValue(cfg(i), "command=")
    if value != "" then
      value
    else
      getCommand(cfg, i+1)


def parseEntry(entry: String, e1: String = "", e2: String = "", i: Int = 0, first: Boolean = true): List[String] =
  if i >= entry.length then
    List(e1, e2)
  else if entry(i) == ':' && first then
    parseEntry(entry, e1, e2, i+1, false)
  else if first then
    parseEntry(entry, e1 + entry(i), e2, i+1, first)
  else
    parseEntry(entry, e1, e2 + entry(i), i+1, first)

// def guessNames(paths: List[String]): List[String] =
//   def getRelativePath(p: String, name: String = "", i: Int = 0): String =
//     if i >= p.length then
//       name
//     else if p(i) == '/' || p(i) == '\\' then
//       getRelativePath(p, "", i+1)
//     else
//       getRelativePath()
//   val supported = (6 to 19).toList
//   val supported_misc = List[Float](7.5)
