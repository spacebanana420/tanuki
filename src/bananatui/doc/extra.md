# Banana TUI

Extra TUI functionality.

```scala
def windows_enableANSI(): Boolean
```
Enables ANSI support for Windows, if not enabled already. Returns true if the command execution works well.

If you intend on running this library on Windows, you most likely have to run this function at the start of your program.

---

```scala
def printProgress(percentage: Byte, str: String = "", first: Boolean = true, usebar: Boolean = true)
```
Prints a progress status that can be updated without clearing the whole screen.

```percentage``` is your progress percentage from 0 to 100.

```str``` is an optional string you can add above the progress status.

```first``` should be set to true if this is your first progress print. For all other consecutive prints, it should be ```false``` instead. The difference is that the progress only updates itself if it's set to false, otherwise it just prints the new progress below the old one. Note that ```str``` should remain always the same for your progress calls.

```usebar``` toggles the use of a progress bar. If you disable it, the progress only shows the percentage.

---

```scala
def searchList(items: Seq[String], keyword: String, casesensitive: Boolean = false): String
```
Searches through a sequence of options/items to find if they match a keyword and then returns the resulting screen string, similar to the chooseOption() functions.

---

```scala
def searchList_read(items: Seq[String], keyword: String, casesensitive: Boolean = false): Int
```
Runs searchList() and prints the returning string and reads the user input, just like chooseOption()
