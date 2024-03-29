package tanuki.quotes
import scala.math.{random, round}

def getRandomQuote(): String =
  val quotes =
    Vector(
    "\"Heaven knows, Earth knows. I know, you know.\" ~ Shikieiki's spellcard message, Touhou 9",
    "\"Go with the flow.\" ~ Reimu's spellcard message, Touhou 9",
    "\"Sing your heart out.\" ~ Mystia Lorelei's spellcard message, Touhou 9",
    "\"Happiness and misfortune come together.\" ~ Yuuka's spellcard message, Touhou 9",
    "\"Dying is loss in death, living is profit in life.\" ~ Komachi's spellcard message, Touhou 9",
    
    "\"Boo!\" ~ Kogasa Tatara, Touhou 12",
    "\"The dead do not speak.\" ~ Sakuya Izayoi, Touhou 7",
    "\"Well, I'm alive.\" ~ Marisa Kirisame, Touhou 9",
    "\"What the hell am I doing here?\" ~ Marisa Kirisame, Touhou 7",
    "\"What's a divine spirit? Does it taste good?\" ~ Kyouko Kasodani, Touhou 13",
    "\"Gensokyo sure is mysterious...\" ~ Sanae Kochiya, Touhou 13",
    "\"I don't intend to tell lies.\" ~ Marisa Kirisame, Touhou 9",
    "\"Are there any fools who want to commit suicide~?\" ~ Komachi Onozuka, Touhou 9 ",
    "\"Well, it'll eventually get better.\" ~ Komachi Onozuka, Touhou 9",
    "\"There are no humans who do not lie.\" ~ Shikieiki Yamaxanadu, Touhou 9",
    "\"When I see the load of work I have to do, I get a little annoyed\" ~ Komachi Onozuka, Touhou 9",
    "\"Ayayayaya...\" ~ Aya Shameimaru, Touhou 10",
    "\"Ah! The mouse! I see you're still alive!\" ~ Sanae Kochiya, Touhou 12",
    "\"Of course, I'm a god.\" ~ Sanae Kochiya, Touhou 12",
    "\"Grhrhgh...\" ~ Aunn Komano, Touhou 16",
    "\"Shut up, deceased one.\" ~ Sakuya Izayoi, Touhou 7",
    "\"My god, jelly donuts are so scary.\" ~ Fujiwara no Mokou, Touhou 8",
    "\"Do you want me to arrange a visit to the Netherworld for you?\" ~ Youmu Konpaku, Touhou 17",
    "\"Whoops, said that out loud. Nothing, it was nothing.\" ~ Reisen Udongein Inaba, Touhou 15",
    "\"I do not particularly intend to fight her. For now.\" ~ Junko, Touhou 15",
    "\"I kinda get it, but I kinda don't.\" ~ Marisa Kirisame, Touhou 15",
    "\"I live my life doing whatever I want.\" ~ Marisa Kirisame, Touhou 16",
    "\"Why not get put to sleep at an animal shelter?\" ~ Sakuya Izayoi, Touhou 7",
    "\"Now, bitch, get out of the way!\" ~ Marisa Kirisame, Touhou 8",
    "\"Ah, now I'm truly flying through space.\" ~ Sanae Kochiya, Touhou 15",
    "\"'Twas a rousing hurlyburly. Thou hast mine gratitude!\" ~ Mononobe no Futo, Touhou 13",
    "\"Didn't you sense that the dream is becoming real?\" ~ Doremy Sweet, Touhou 15",
    "\"Oh, I'm so sorry. I forgot.\" ~ Chimata Tenkyuu, Touhou 18",
    "\"Sorry, but I'm afraid that I'm already half-dead.\" ~ Youmu Konpaku, Touhou 13",
    "\"What was that weird stuff you were saying before?\" ~ Marisa Kirisame, Touhou 14",
    "\"Well... maybe I'm still a little crazy.\" ~ Reisen Udongein Inaba, Touhou 15",
    "\"If I had to say, I think I'd be the one making people cry, y'know~?\" ~ Clownpiece, Touhou 15",
    "\"Anyways, have you seen a cat around these parts, by any chance?\" ~ Ran Yakumo, Touhou 7",
    "\"Th-the shrine maiden!? Oh, how did I run into this menace.\" ~ Nazrin, Touhou 19",
    "\"Well, I'm not so rusty that I'd lose to a mouse.\" ~ Marisa Kirisame, Touhou 19",
    "\"Why are all shrine maidens so violent?\" ~ Aunn Komano, Touhou 19",
    "\"But from now on, I'll be following miss Suika's advice and get you to scold me.\" ~ Hisami Yomotsu, Touhou 19",
    "\"Dying's no good. Anything but that.\" ~ Yoshika Miyako, Touhou 13",
    "\"Do you hate hell? If so, then losing to me would be a bit...\" ~ Hisami Yomotsu, Touhou 19",
    "\"Everywhere I go, flowers, flowers, and more flowers.\" ~ Yuuka Kazami, Touhou 9",
    "\"A first warning for a human hurrying to their death.\" ~ Komachi Onozuka, Touhou 9",
    "\"Er, um, I'll just pretend I didn't see that.\" ~ Komachi Onozuka, Touhou 9",
    "\"Hah! What exactly was I doing?\" ~ Youmu Konpaku, Touhou 9",
    "\"I'll ferry you over with my Titanic. 50% off.\" ~ Komachi Onozuka, Touhou 9",
    "\"Who's the one making that annoying sound in my territory?\" ~ Mystia Lorelei, Touhou 9",
    "\"You're so loud in the morning.\" ~ Cirno, Touhou 9",
    "\"Go do your work! Stop slacking off all the time!\" ~ Reimu Hakurei, Touhou 9",
    "\"I've got some nice cards! Buy something for the road?\" ~ Mike Goutokuji, Touhou 18",
    "\"I have helped myself to your delicious nightmares.\" ~ Doremy Sweet, Touhou 15",
    "\"A ten-game match of danmaku transformations! Shall we begin?\" ~ Mamizou Futatsuiwa, Touhou 13",
    "\"Ooh, so you don't mind if I beat you?\" ~ Marisa Kirisame, Touhou 13",
    "\"Now attempt to defeat me! And lay bare your own desire!\" ~ Toyosatomimi no Miko, Touhou 13",
    "\"I guess this means everyone's relying on me, huh?\" ~ Sanae Kochiya, Touhou 15",
    "\"Someone visited my place by mistake the other day. I gave them fried tofu.\" ~ Aunn Komano, Touhou 19",
    "\"Some visitor the other day thought I was a fox and gave me fried tofu.\" ~ Aunn Komano, Touhou 19",
    "\"Is it over? If it's over, then I wanna drink some sake!\" ~ Mamizou Futatsuiwa, Touhou 19",
    "\"How cold of you.\" ~ Marisa Kirisame, Touhou 6",
    "\"Ah, so you're the human who gave Chen a hard time?\" ~ Ran Yakumo, Touhou 7",
    "\"Preparations are fun? You're a weirdo.\" ~ Marisa Kirisame, Touhou 7",
    "\"...it's still too early to speak.\" ~ Sagume Kishin, Touhou 15",
    "\"It's my job, after all. I have to do it.\" ~ Reisen Udongein Inaba, Touhou 15"
    )
  val i = round(random() * (quotes.length-1)).toInt
  quotes(i)
