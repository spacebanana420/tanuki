package tanuki.tui


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