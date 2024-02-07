package tanuki.config

import java.io.File
import java.io.FileOutputStream


def writeConfig(cfg: String) =
  FileOutputStream("config.txt").write(cfg.getBytes())
