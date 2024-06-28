package tanuki.quotes
import scala.math.{random, round}

def getRandomQuote(): String =
  val quotes =
    Vector(
    // Touhou 6
    "\"How cold of you.\" ~ Marisa Kirisame, Touhou 6",
    
    // Touhou 7
    "\"The dead do not speak.\" ~ Sakuya Izayoi, Touhou 7",
    "\"What the hell am I doing here?\" ~ Marisa Kirisame, Touhou 7",
    "\"Ahh, it's getting a lot warmer.\" ~ Marisa Kirisame, Touhou 7",
    "\"Shut up, deceased one.\" ~ Sakuya Izayoi, Touhou 7",
    "\"Why not get put to sleep at an animal shelter?\" ~ Sakuya Izayoi, Touhou 7",
    "\"Anyways, have you seen a cat around these parts, by any chance?\" ~ Ran Yakumo, Touhou 7",
    "\"Ah, so you're the human who gave Chen a hard time?\" ~ Ran Yakumo, Touhou 7",
    "\"...sob sob...\" ~ Ran Yakumo, Touhou 7",
    "\"Preparations are fun? You're a weirdo.\" ~ Marisa Kirisame, Touhou 7",
    
    // Touhou 8
    "\"My god, jelly donuts are so scary.\" ~ Fujiwara no Mokou, Touhou 8",
    "\"Now, bitch, get out of the way!\" ~ Marisa Kirisame, Touhou 8",
    
    // Touhou 9
    "\"Well, I'm alive.\" ~ Marisa Kirisame, Touhou 9",
    "\"I don't intend to tell lies.\" ~ Marisa Kirisame, Touhou 9",
    "\"Are there any fools who want to commit suicide~?\" ~ Komachi Onozuka, Touhou 9 ",
    "\"Well, it'll eventually get better.\" ~ Komachi Onozuka, Touhou 9",
    "\"There are no humans who do not lie.\" ~ Shiki Eiki Yamaxanadu, Touhou 9",
    "\"When I see the load of work I have to do, I get a little annoyed\" ~ Komachi Onozuka, Touhou 9",
    "\"Everywhere I go, flowers, flowers, and more flowers.\" ~ Yuuka Kazami, Touhou 9",
    "\"A first warning for a human hurrying to their death.\" ~ Komachi Onozuka, Touhou 9",
    "\"Er, um, I'll just pretend I didn't see that.\" ~ Komachi Onozuka, Touhou 9",
    "\"Hah! What exactly was I doing?\" ~ Youmu Konpaku, Touhou 9",
    "\"I'll ferry you over with my Titanic. 50% off.\" ~ Komachi Onozuka, Touhou 9",
    "\"Who's the one making that annoying sound in my territory?\" ~ Mystia Lorelei, Touhou 9",
    "\"You're so loud in the morning.\" ~ Cirno, Touhou 9",
    "\"Go do your work! Stop slacking off all the time!\" ~ Reimu Hakurei, Touhou 9",
    "\"I'm sorry! I won't do it anymore. I'll seriously work, so~!\" ~ Komachi Onozuka, Touhou 9",
    
    // Touhou 9  (Spell card message)
    "\"Heaven knows, Earth knows. I know, you know.\" ~ Shiki Eiki's spell card message, Touhou 9",
    "\"Go with the flow.\" ~ Reimu's spell card message, Touhou 9",
    "\"Sing your heart out.\" ~ Mystia Lorelei's spell card message, Touhou 9",
    "\"Happiness and misfortune come together.\" ~ Yuuka's spell card message, Touhou 9",
    "\"Dying is loss in death, living is profit in life.\" ~ Komachi's spell card message, Touhou 9",
    
    // Touhou 10
    "\"Ayayayaya...\" ~ Aya Shameimaru, Touhou 10",
    "\"Oof...\" ~ Hina Kagiyama, Touhou 10",
    "\"Oh, are you still here?\" ~ Hina Kagiyama, Touhou 10",
    
    // Touhou 12
    "\"Boo!\" ~ Kogasa Tatara, Touhou 12",
    "\"Of course, I'm a god.\" ~ Sanae Kochiya, Touhou 12",
    "\"Ah! The mouse! I see you're still alive!\" ~ Sanae Kochiya, Touhou 12",
    "\"What, you mean I'm behind the times?\" ~ Kogasa Tatara, Touhou 12",
    "\"Oh, I'm so lonely.\" ~ Kogasa Tatara, Touhou 12",
    "\"I want to create a world where humans and youkai live as equals.\" ~ Byakuren Hijiri, Touhou 12",
    "\"Better prepare myself for the worst.\" ~ Marisa Kirisame, Touhou 12",
    "\"Oh, so you're saying to go back to square one! Maybe that's not a bad idea...\" ~ Kogasa Tatara, Touhou 12",
    "\"Ee! A mouse, a mouse!\" ~ Sanae Kochiya, Touhou 12",
    "\"A treasury? I like the sound of that.\" ~ Marisa Kirisame, Touhou 12",
    "\"You're just sorry you lost.\" ~ Marisa Kirisame, Touhou 12",
    "\"What? You're a magician? You're in the business?\" ~ Marisa Kirisame, Touhou 12",
    
    // Touhou 13
    "\"What's a divine spirit? Does it taste good?\" ~ Kyouko Kasodani, Touhou 13",
    "\"Gensokyo sure is mysterious...\" ~ Sanae Kochiya, Touhou 13",
    "\"'Twas a rousing hurlyburly. Thou hast mine gratitude!\" ~ Mononobe no Futo, Touhou 13",
    "\"Sorry, but I'm afraid that I'm already half-dead.\" ~ Youmu Konpaku, Touhou 13",
    "\"Dying's no good. Anything but that.\" ~ Yoshika Miyako, Touhou 13",
    "\"A ten-game match of danmaku transformations! Shall we begin?\" ~ Mamizou Futatsuiwa, Touhou 13",
    "\"Ooh, so you don't mind if I beat you?\" ~ Marisa Kirisame, Touhou 13",
    "\"Now attempt to defeat me! And lay bare your own desire!\" ~ Toyosatomimi no Miko, Touhou 13",
    "\"This won't result in a conversation. Is your brain rotten?\" ~ Marisa Kirisame, Touhou 13",
    "\"Whaaaaat?!! I'm dyiiiiing!\" ~ Yoshika Miyako, Touhou 13",
    "\"Yes, yes, we get it, you're already dead.\" ~ Youmu Konpaku, Touhou 13",
    "\"No, uh, I can die.\" ~ Youmu Konpaku, Touhou 13",
    
    // Touhou 14
    "\"What was that weird stuff you were saying before?\" ~ Marisa Kirisame, Touhou 14",
    "\"I started wanting to cut something, like a neck...\" ~ Sakuya Izayoi, Touhou 14",
    
    // Touhou 15
    "\"Whoops, said that out loud. Nothing, it was nothing.\" ~ Reisen Udongein Inaba, Touhou 15",
    "\"I do not particularly intend to fight her. For now.\" ~ Junko, Touhou 15",
    "\"I kinda get it, but I kinda don't.\" ~ Marisa Kirisame, Touhou 15",
    "\"Ah, now I'm truly flying through space.\" ~ Sanae Kochiya, Touhou 15",
    "\"Didn't you sense that the dream is becoming real?\" ~ Doremy Sweet, Touhou 15",
    "\"Well... maybe I'm still a little crazy.\" ~ Reisen Udongein Inaba, Touhou 15",
    "\"If I had to say, I think I'd be the one making people cry, y'know~?\" ~ Clownpiece, Touhou 15",
    "\"I have helped myself to your delicious nightmares.\" ~ Doremy Sweet, Touhou 15",
    "\"I guess this means everyone's relying on me, huh?\" ~ Sanae Kochiya, Touhou 15",
    "\"...it's still too early to speak.\" ~ Sagume Kishin, Touhou 15",
    "\"It's my job, after all. I have to do it.\" ~ Reisen Udongein Inaba, Touhou 15",
    
    // Touhou 16
    "\"Grhrhgh...\" ~ Aunn Komano, Touhou 16",
    "\"I live my life doing whatever I want.\" ~ Marisa Kirisame, Touhou 16",
    
    // Touhou 16.5
    "\"You poor thing... Live strong.\" ~ Doremy Sweet, Touhou 16.5",
    
    // Touhou 17
    "\"Do you want me to arrange a visit to the Netherworld for you?\" ~ Youmu Konpaku, Touhou 17",
    "\"Fu fu fu.\" ~ Keiki Haniyasushin, Touhou 17",
    
    // Touhou 18
    "\"Oh, I'm so sorry. I forgot.\" ~ Chimata Tenkyuu, Touhou 18",
    "\"I've got some nice cards! Buy something for the road?\" ~ Mike Goutokuji, Touhou 18",
    
    // Touhou 19
    "\"Th-the shrine maiden!? Oh, how did I run into this menace.\" ~ Nazrin, Touhou 19",
    "\"Well, I'm not so rusty that I'd lose to a mouse.\" ~ Marisa Kirisame, Touhou 19",
    "\"Why are all shrine maidens so violent?\" ~ Aunn Komano, Touhou 19",
    "\"But from now on, I'll be following miss Suika's advice and get you to scold me.\" ~ Hisami Yomotsu, Touhou 19",
    "\"Do you hate hell? If so, then losing to me would be a bit...\" ~ Hisami Yomotsu, Touhou 19",
    "\"Someone visited my place by mistake the other day. I gave them fried tofu.\" ~ Aunn Komano, Touhou 19",
    "\"Some visitor the other day thought I was a fox and gave me fried tofu.\" ~ Aunn Komano, Touhou 19",
    "\"Is it over? If it's over, then I wanna drink some sake!\" ~ Mamizou Futatsuiwa, Touhou 19",
    )
  val i = round(random() * (quotes.length-1)).toInt
  quotes(i)
