# Downloading and running Tanuki

Tanuki is compiled as Java bytecode, which means it runs on the Java virtual machine. Exceptionally, a native x86_64 Linux binary is provided as alternative.

## Information on download options

### tanuki.jar

This is a lightweight JAR that requires to be run with Scala or Scala-CLI, as it lacks the Scala standard library.

To run Tanuki, you can launch the jar with:

```
scala tanuki.jar
```
or
```
scala-cli tanuki.jar
```

### tanuki-java.jar

This is a fat jar, as you can tell by the file size compared to tanuki.jar. The size difference is caused by including the Scala standard library inside the JAR, making it fully self-contained. Scala is not required to run Tanuki in this form, all you need is Java version 11 or above.

To run Tanuki, you can launch the jar with:

```
java -jar tanuki.jar
```

### tanuki-linux-x86_64.zip

This archive contains a native binary for x86_64 Linux-based systems, named "tanuki". It is fully statically-linked, which means it requires no external dependencies. It will run properly on any desktop or server Linux operating system due to its static nature. The binary is on the larger side, because the Scala and Java standard libraries are compiled natively into it. The binary is built with GraalVM, but unfortunately this amazing tool doesn't support cross-compiling, so I will only distribute for x86_64 Linux. If you don't run an x86_64 architecture or a Linux-based system, you have to use the jar versions.

You can run Tanuki with this command:

```
./tanuki
```

<p align="center">
<img src="../images/boooo.png" height="230"/>
</p>


