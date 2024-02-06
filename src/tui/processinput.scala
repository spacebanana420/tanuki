package tanuki.tui


def answerToNumber(str: String): Int =
  try
    str.toInt
  catch
    case e: Exception => -1
