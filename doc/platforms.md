# Tanuki's platform support

Tanuki is compiled for the Java Virtual Machine, becoming in theory OS-agnostic and CPU-agnostic, but Touhou games are compiled for x86 CPUs, and so Tanuki as Touhou launcher can only be useful for x86 machines.

### Tanuki's software requirements

* Scala 3 or Java 11 or later (unless if on x86_64 Linux)
* FFmpeg (optional, for all functionality related to image and video)
* WINE (optional for Windows, for running Touhou on non-Windows systems)
* DXVK (optional, for setting dxvk_framerate in Tanuki's configuration)
  * Tanuki does not configure DXVK for the user
* steam-run (optional, for NixOS only and only if you run third-party wine binaries)
* xdg-utils (optional, for opening directories and configs with your default desktop application) (not available for Windows or MacOS)

Depending on the JAR you download, you either require Scala 3 to be in your system or just Java.

### Officially supported systems
* Linux-based systems
* FreeBSD

These operating systems are officially supported by me. If there's something wrong with them, please let me know, I'll look into the problem ASAP.

On FreeBSD, in order to view images and videos, you must build the FFmpeg port with SDL support enabled to have FFplay, the **FFmpeg binary pkg does not provide FFplay**!

### Theoretically/partially supported systems
* Windows
* MacOS
* NetBSD
* OpenBSD
* Other *BSDs, unixes and the sorts

I don't guarantee support on these systems, but I believe they might work without hassle, as they support Java, FFmpeg and WINE (except Windows).

On Windows, it's not recommended that you run Tanuki with the default terminal that comes with Powershell, because it does not support ANSI escape codes, and so the interface doesn't work properly at all.

More information on video recording support can be seen in the [recorder documentation](recorder.md).

<p align="center">
<img src="../images/futo.png" height="230"/>
</p>
