package tanuki.config

import java.io.File
import java.io.FileOutputStream

def createConfig() = FileOutputStream("config.txt")

def writeConfig(cfg: List[String], append: Boolean = true) =
  def createStr(str: String = "", i: Int = 0): String =
    if i >= cfg.length then
      str
    else if cfg(i) == "" then
      createStr(str, i+1)
    else
      createStr(str + cfg(i) + '\n', i+1)

  val cfgstr = createStr()
  if cfgstr != "" then
    FileOutputStream("config.txt", append).write(cfgstr.getBytes())
