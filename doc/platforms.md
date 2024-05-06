# Tanuki's platform support

Tanuki is compiled for the Java Virtual Machine, becoming in theory OS-agnostic and CPU-agnostic. But Touhou games are compiled for x86 CPUs, and so Tanuki can only be useful for x86 machines.

### Tanuki's software requirements

* Scala 3 or Java 11 or later
* FFmpeg (optional, for screen recording and screenshot functionality)
* FFplay (optional, for viewing video and images)
* WINE (optional for Windows, for running Touhou on non-Windows systems)
* steam-run (optional, for NixOS only and only if you run third-party wine binaries)

Depending on the JAR you download, you either require Scala 3 to be in your system or just Java.

### Officially supported systems
* Linux-based systems
* FreeBSD
* Windows

These operating systems are officially supported by me. If there's something wrong with them, please let me know, I'll look into the problem ASAP.

On Windows, it's not recommended that you run Tanuki with the default terminal that comes with powershell, because it does not support ANSI escape codes, and so colors don't work and you will see a lot of gibberish on the interface instead.

On FreeBSD, in order to view images and videos, you must build the FFmpeg port with SDL support enabled to have FFplay, the **FFmpeg binary pkg does not provide FFplay**!

### Theoretically/partially supported systems
* MacOS
* NetBSD

I don't guarantee support on these systems, but I believe they might work without hassle, as they support Java, FFmpeg and WINE.

More information on video recording support can be seen in the [recorder documentation](recorder.md).

<p align="center">
<img src="../images/futo.png" height="230"/>
</p>
