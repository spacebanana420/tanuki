package tanuki.misc

import bananatui.*
import sys.process.*

private def findMatch(find: String, supported: Seq[String], exact_match: Boolean, i: Int = 0): Boolean =
  if i >= supported.length then
    false
  else if (find == supported(i) && exact_match) || (find.contains(supported(i)) && !exact_match) then
    true
  else
    findMatch(find, supported, exact_match, i+1)


def belongsToList(find: String, supported: Seq[String]): Boolean = findMatch(find, supported, true)
def similarInList(find: String, supported: Seq[String]): Boolean = findMatch(find, supported, false)


def xdg_open(path: String): Boolean =
  try
    Vector("xdg-open", path).run(ProcessLogger(line => ()))
    true
  catch case e: Exception =>
    pressToContinue("Failed to open the file!\nMaybe your system does not have xdg-utils installed!")
    false
