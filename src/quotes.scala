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
  "\"Well, it'll eventually get better\" ~ Komachi Onozuka, Touhou 9",
  "\"There are no humans who do not lie.\" ~ Shikieiki Yamaxanadu, Touhou 9",
  "\"When I see the load of work I have to do, I get a little annoyed\" ~ Komachi Onozuka, Touhou 9",
  "\"Ayayayaya...\" ~ Aya Shameimaru, Touhou 10",
  "\"Ah! The mouse! I see you're still alive!\" ~ Sanae Kochiya, Touhou 12",
  "\"Of course, I'm a god.\" ~ Sanae Kochiya, Touhou 12",
  "\"Grhrhgh...\" ~ Aunn Komano, Touhou 16",
  "\"Shut up, deceased one.\" ~ Sakuya Izayoi, Touhou 7",
  "\"My god, jelly donuts are so scary.\" ~ Fujiwara no Mokou, Touhou 8",
  "\"Do you want me to arrange a visit to the Netherworld for you?\" ~ Youmu Konpaku, Touhou 17",
  "\"Whoops, said that out loud. Nothing, it was nothing.\" ~ Reisen Udongein Inaba, Touhou 15"
  )
  val i = round(random() * (quotes.length-1)).toInt
  quotes(i)
