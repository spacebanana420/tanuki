package tanuki.config

import java.io.File
import scala.io.Source

def isConfigOK(cfg: List[String], entries: List[String], paths: List[String]): Boolean =

  val areSettingsEqual =
    if entries.length == paths.length then
      true
    else
      false
