# Compile Tanuki from source

You need [Scala 3](https://scala-lang.org/) to build Tanuki from source. You can use the scalac compiler or scala-cli

## Using scalac

```
scalac src/*.scala src/*/*.scala src/*/*/*.scala -d tanuki.jar
```
This builds a lightweight JAR that can be run with the scala command or scala-cli.

You can now run this JAR with Scala or scala-cli.

## Using scala-cli

### Option 1
```
scala-cli --power package src --library -o tanuki.jar
```
Lightweight JAR, but only scala-cli can run this JAR.

### Option 2
```
scala-cli --power package src --assembly --preamble=false -o tanuki-fat.jar
```
This is an assembly JAR, containing all code dependencies. Yyu can run this JAR with Java directly.

You can specify the target JVM version with the ```--jvm``` argument (example: ```--jvm 8```).

### Option 3
```
scala-cli src
```
This compiles and runs Tanuki, keeping the compiled bytecode in a hidden directory inside "src".

Next time you run this command, it will run the already-compiled program, no need for additional compiles.

If you just want to compile but not run, you can run the command:

```
scala-cli compile src
```
