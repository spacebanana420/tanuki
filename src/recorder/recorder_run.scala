package tanuki.recorder

import ffscala.capture.*


def recordVideo(output: String, args: List[String], delay: Int = 0) =
  if delay > 0 then Thread.sleep(delay*1000)
  record(output, args)
