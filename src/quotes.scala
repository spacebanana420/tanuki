package tanuki.quotes
import scala.math.{random, round}

def getRandomQuote(): String =
  val quotes =
  List(
  "\"Heaven knows, Earth knows. I know, you know.\" ~ Shikieiki's spellcard message, Touhou 9",
  "\"Boo!\" ~ Kogasa Tatara, Touhou 12",
  "\"The dead do not speak.\" ~ Sakuya Izayoi, Touhou 7",
  "\"Well, I'm alive.\" ~ Marisa Kirisame, Touhou 9",
  "\"What the hell am I doing here?\" ~ Marisa Kirisame, Touhou 7",
  "\"What's a divine spirit? Does it taste good?\" ~ Kyouko Kasodani, Touhou 13",
  "\"Gensokyo sure is mysterious...\" ~ Sanae Kochiya, Touhou 13",
  "\"I don't intend to tell lies.\" ~ Marisa Kirisame, Touhou 9",
  "\"Are there any fools who want to commit suicide~?\" ~ Komachi Onozuka, Touhou 9 ",
  "\"Well, it'll eventually get better\" ~ Komachi Onozuka, Touhou 9"
  )
  val i = round(random() * quotes.length-1).toInt
  quotes(i)
