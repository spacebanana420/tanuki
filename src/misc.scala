package tanuki.misc

private def findMatch(find: String, supported: List[String], exact_match: Boolean, i: Int = 0): Boolean =
  if i >= supported.length then
    false
  else if (find == supported(i) && exact_match) || (find.contains(supported(i)) && !exact_match) then
    true
  else
    findMatch(find, supported, exact_match, i+1)


def belongsToList(find: String, supported: List[String]): Boolean = findMatch(find, supported, true)
def similarInList(find: String, supported: List[String]): Boolean = findMatch(find, supported, false)
