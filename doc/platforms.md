# Tanuki's platform support

Tanuki is compiled for the Java Virtual Machine, becoming in theory OS-agnostic and CPU-agnostic. But Touhou games are compiled for x86 CPUs, and so Tanuki can only be useful for x86 machines.

### Tanuki's software requirements

* Scala 3 or Java 8 or later
* FFmpeg (optional, for screen recording, video playback and screenshot functionality)
* WINE (for running Touhou on non-Windows systems)
* steam-run (for NixOS only and only if you run third-party wine binaries)

Depending on the JAR you download, you either require Scala 3 to be in your system or just Java.

### Officially supported systems
* Linux-based systems
* Windows

These operating systems are officially supported by me. If there's something wrong with them, please let me know, I'll look into the problem ASAP.

On Windows, it's not recommended that you run Tanuki with the default terminal that comes with powershell, because it does not support ANSI escape codes, and so colors don't work and you will see a lot of gibberish instead.

### Theoretically supported systems
* FreeBSD
* MacOS
* NetBSD

I don't guarantee support or test Tanuki on these systems, but I believe they might work without hassle, as they support Java, FFmpeg and WINE.

Note that Tanuki's video recorder does not support MacOS, and Linux, FreeBSD and NetBSD are only supported if your system is running x11 and Pulseaudio.

<p align="center">
<img src="../images/futo.png" height="230"/>
</p>
