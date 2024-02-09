# Tanuki's platform support

Tanuki is compiled for the Java Virtual Machine, becoming in theory OS-agnostic and CPU-agnostic. But Touhou games are compiled for x86 CPUs, and so Tanuki can only be useful for x86 machines.

### Tanuki's software requirements

* Scala 3 or Java 19
* FFmpeg (optional, for screenshot functionality)
* WINE (for non-Windows systems)
* steam-run (for NixOS only and only if you run third-party wine binaries)

### Officially supported systems
* Windows
* Linux-based systems

These operating systems are officially supported by me. If there's something wrong with them, please let me know, I'll look into the problem ASAP.

### Theoretically supported systems
* FreeBSD
* MacOS
* NetBSD

I don't explicitly support or test Tanuki on these systems, but I believe they might work without hassle, as they support Java, FFmpeg and WINE.
