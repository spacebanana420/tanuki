# Banana TUI

Functions for reading user input, printing options and letting the user choose them, etc.

## User input functions

```scala
def readUserInput(message: String = ""): String
```
Reads user input. If ```message``` is not an empty string, it also prints it.

---

```scala
def spawnAndRead(message: String): String
```
Clears the screen, prints ```message``` and returns the user input.

---

```scala
def askPrompt(ui: String, clear: Boolean = true): Boolean
```
Clears the screen, prints ```message``` with an additional (y/n) and returns true or false, depending if the user answered y/yes or not.

---

```scala
def answerToNumber(str: String): Int
```
Attempts to convert a string to an int. If the string does not represent an int, it returns -1.

---

```scala
def answerToShort(str: String): Int
```
Attempts to convert a string to a short. If the string does not represent a short, it returns -1.

---

```scala
def answerToByte(str: String): Int
```
Attempts to convert a string to a byte. If the string does not represent a byte, it returns -1.

---

```scala
def pressToContinue(message: String = ""): String
```
Prints ```message``` with an additional "press enter to continue" and reads user input.

---

```scala
def readLoop(txt: String, maxval: Int): Int
```
Prints ```txt``` and reads the user's answer. If the answer is not a number, 0 or below or equal than ```maxval```, the user has to answer again.

---

```scala
def chooseOption(l: Seq[String], title: String = "Choose an entry", first: String = "Exit"): Int
```
Groups all elements of ```l``` together nicely, with a ```title``` above, and the first option of name ```first```. The user has to select the value of an option.

The function returns the value the user chose.

---

```scala
def chooseOption_array(l: Array[String], title: String = "Choose an entry", first: String = "Exit"): Int
```
Just lik ```chooseOption()```, but with arrays.

Groups all elements of ```l``` together nicely, with a ```title``` above, and the first option of name ```first```. The user has to select the value of an option.

The function returns the value the user chose.

---

```scala
def chooseOption_string(l: Seq[String], title: String = "Choose an entry", first: String = "Exit"): String
```
```scala
def chooseOption_astring(l: Array[String], title: String = "Choose an entry", first: String = "Exit"): String
```
Just like ```chooseOption()```, but it's nicer to use and you get the list element directly.

Groups all elements of ```l``` together nicely, with a ```title``` above, and the first option of name ```first```. The user has to select the value of an option.

The function returns the element of ```l``` the user chose, or an empty string if the choice was 0.

---

```scala
def chooseOption_h(l: Seq[String], size: Int = 3, title: String = "Choose an entry", first: String = "Exit"): Int
```
Very similar to ```chooseOption()```, but options are listed horizontally, with ```size``` elements per line.

Groups all elements of ```l``` together nicely, with a ```title``` above, and the first option of name ```first```. The user has to select the value of an option.

The function returns the value the user chose.

---

```scala
def chooseOption_hs(l: Seq[String], size: Int = 3, title: String = "Choose an entry", first: String = "Exit"): Int
```
Very similar to ```chooseOption()_string```, but options are listed horizontally, with ```size``` elements per line.

Groups all elements of ```l``` together nicely, with a ```title``` above, and the first option of name ```first```. The user has to select the value of an option.

The function returns the string element, or an empty string if the user chooses the option 0.

---

```scala
def readInt(txt: String): Int
```
Prints ```txt``` and reads the user input, if the input is not an int, it keeps reading.

---

```scala
def readShort(txt: String): Int
```
Prints ```txt``` and reads the user input, if the input is not a short, it keeps reading.

---

```scala
def readByte(txt: String): Int
```
Prints ```txt``` and reads the user input, if the input is not a byte, it keeps reading.

---

```scala
def chooseOption_dir(txt: String): String
```
Similar to ```chooseOption()```, but the user has to type the path to a directory. If the answer is an empty string, the function assumes the current working directory.

If the answer does not lead to a directory, the function asks for input.

---

```scala
def chooseOption_file(txt: String): String
```
Similar to ```chooseOption()```, but the user has to type the path to a file.

If the answer does not lead to a file, the function asks for input again.
