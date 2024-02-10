# Tanuki's platform support

Tanuki is compiled for the Java Virtual Machine, becoming in theory OS-agnostic and CPU-agnostic. But Touhou games are compiled for x86 CPUs, and so Tanuki can only be useful for x86 machines.

### Tanuki's software requirements

* Scala 3 or Java 19
* FFmpeg (optional, for screenshot functionality)
* WINE (for running Touhou on non-Windows systems)
* steam-run (for NixOS only and only if you run third-party wine binaries)

Depending on the JAR you download, you either require Scala 3 to be in your system or just Java. As for the Java-only requirement, for now Java 19 or later is required. Due to a bug with my tooling, I can't yet target an older JVM version without a few extra steps. I'll do that as soon as I can.

### Officially supported systems
* Linux-based systems
* Windows

These operating systems are officially supported by me. If there's something wrong with them, please let me know, I'll look into the problem ASAP.

### Theoretically supported systems
* FreeBSD
* MacOS
* NetBSD

I don't explicitly support or test Tanuki on these systems, but I believe they might work without hassle, as they support Java, FFmpeg and WINE.

<p align="center">
<img src="../images/futo.png" width="290"/>
</p>
