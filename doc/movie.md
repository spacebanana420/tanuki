# Gensokyo Cinema

After you record your gameplay footage, you can view it inside Tanuki.

Tanuki uses FFplay for viewing videos, and so FFmpeg is required. The path to your videos is the same as the path to your recording footage, and you can view all footage with "tanuki-video" in their file name.

## Controls

* Seeking: Arrow keys
* Volume: 0 and 9 keys
* Pause: P or space
* Quit: Q or ESC
* Fullscreen: F
* Change view mode: W

You can also seek to a certain place by clicking with the right mouse button. The horizontal position of the cursor defines the point in the video's time.

## My video's colors look weird!

If you recorded in QSV MJPEG, your video should look like it has low contrast and saturation when you view it in Tanuki. This is just a flaw in the default color management (or lack of it) in FFplay, your video is fine. Try to view it with another player (that supports MJPEG) and the colors should look fine.

If you record in QSV H.264 or H.265, there is some slight destruction to color fidelity. Besides the color subsampling itself, red tones should be slightly shifted towards orange. 

If you recorded in QSV H.265, you probably noticed that your footage takes a long time to load and it looks destroyed. Your video is fine, it's just that it's very heavy to decode and so the media player can't keep up very well.

## Tanuki doesn't find my video footage!

Tanuki will look for the path you use for recording, and detect all files with "tanuki-video" in the name. If it doesn't find any video you recorded, maybe you changed the recording path and now it's looking for the new one, or your recording failed due to incorrect encoding configurations.

<p align="center">
<img src="../images/doremy.png" height="230"/>
</p>