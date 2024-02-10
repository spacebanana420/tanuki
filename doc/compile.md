# Compile Tanuki from source

You need [Scala 3](https://scala-lang.org/) to build Tanuki from source. You can use the scalac compiler or scala-cli

## Using scalac

```
scalac src/*.scala src/*/*.scala src/*/*/*.scala -d tanuki.jar
```
This builds a lightweight JAR that can be run with the scala command or scala-cli. You do not need an internet connection.

You can now run this JAR with Scala or scala-cli.

## Using scala-cli

scala-cli is a powerful tool that opens potential for much more than using the scalac compiler directly. Note that you need an internet connection the first time you compile with scala-cli.

### Option 1 - Straightforward way

```
scala-cli src
```
This compiles and runs Tanuki, keeping the compiled bytecode in a hidden directory inside "src".

Next time you run this command, it will run the already-compiled program, no need for additional compiles.

If you just want to compile but not run, you can run the command:

```
scala-cli compile src
```

### Option 2 - Lightweight JAR

```
scala-cli --power package src --library -o tanuki.jar
```
Lightweight JAR, but only scala-cli can run this JAR.

### Option 3 - Assembly JAR

```
scala-cli --power package src --assembly --preamble=false -o tanuki-fat.jar
```
This is an assembly JAR, containing all code dependencies. You can run this JAR with Java directly.

You can specify the target JVM version with the ```--jvm``` argument (example: ```--jvm 8```).

### Option 4 - Native binary

The following requires that you install Clang/LLVM in your system:
```
scala-cli --power package src --native -o tanuki
```
This compiles Tanuki into a native binary executable for your OS and CPU, resulting in instant startup times, faster speed and lower memory use.

If you do not want to install Clang/LLVM, you can use the alternative method:

```
scala-cli --power package src --native-image -o tanuki
```
This method only depends on GraalVM, which is automatically downloaded by scala-cli.

<p align="center">
<img src="../images/congratulations.png" width="290"/>
</p>
