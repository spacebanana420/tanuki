# Banana TUI

Base functionality for printing, clearing, moving the terminal cursor, changing terminal colors, etc.

## Main functions

```scala
def spawnScreen(ui: String)
```
Clears the screen and the terminal history and prints ```ui```.

---

```scala
def clear()
```
Clears the terminal screen and history.

---

```scala
def saveScreen()
```
Saves the current terminal screen.

---

```scala
def restoreScreen()
```
Restores the terminal screen that was previously saved.

---

```scala
def moveCursor(mode: String, lines: Int)
```
Moves the cursor up and down, depending on the mode.

If mode is "up" then the cursor moves up, otherwise it goes down.

---

```scala
def clearBelowCursor(lines: Int)
```
Clear a certain amount of lines below the cursor.

---

```scala
def printStatus(msg: String, isError: Boolean = true)
```
Prints a fancy error or warning message. Set ```isError``` to false to print a warning.

---

## Color functions

```scala
def foreground(color: String = "default"): String
```
Returns the code to set the foreground color in the terminal, that being the text color.

The function does not set a specific color, instead it sets the color equivalent to your terminal's palette.

To set the color, use the string this function returns in your next print.

### Supported colors
* black
* red
* green
* yellow
* blue
* magenta
* cyan
* white
* default
* reset

Default is what the function returns if you pass an incorrect color string. Default is also what is used for text in your terminal by default.

Running the function won't set the color by itself, it instead returns the color code that can be used to set the color. This lets you be extremely flexible and interpolate the strings however you want.

### Example:
```scala
println(s"${foreground("red")}This is red ${foreground("default")} and this is back to normal")
```

---

```scala
def background(color: String = "default"): String
```
Returns the code to set the backgroun color in the terminal.

The function does not set a specific color, instead it sets the color equivalent to your terminal's palette.

To set the color, use the string this function returns in your next print.

### Supported colors
* black
* red
* green
* yellow
* blue
* magenta
* cyan
* white
* default
* reset

Default is what the function returns if you pass an incorrect color string.

Running the function won't set the color by itself, it instead returns the color code that can be used to set the color. This lets you be extremely flexible and interpolate the strings however you want.

### Example:
```scala
println(s"${background("red")}This is red ${background("default")} and this is back to normal")
```

---
