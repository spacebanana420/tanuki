# Tanuki's video recorder

With Tanuki, you can record your Touhou gameplay in the background into a video.

A recording must have a video and audio channel and does not support more than one yet.

Once you configure your recording setup from within the launcher, you can play your game while you record your replay. A video named "tanuki-video.mov" will be saved in your ```output``` path. Tanuki saves its recorder configuration to ```video_config.txt```.

As of now, it is only possible to record your whole screen and not a specific window.

## Supported platforms
* Linux-based systems under x11 and pulseaudio
* Any other system under x11 and pulseaudio (untested)

Support for Windows, MacOS and other capture backends is on my plans but it is not implemented yet.

## Configuration options:

* ```output``` - The path to where you save your video recording.
  * The path must lead to a directory.
* ```delay``` - The delay, in seconds, to when the recording should start after you launch the game.
  * Maximum value is 60 seconds.
* ```vcodec``` - Your video encoder selection and its parameters.
* ```aacodec``` - Your audio encoder selection and its parameters.
* ```vcapture``` - Your screen capture configuration.
* ```acapture``` - Your audio capture configuration.

The video and audio encoding parameters are unique to each encoder.

## How to properly capture gameplay

Tanuki only captures the whole screen, unable to capture specific windows. Because of this, your video's width and height should correspond to the resolution you will be playing Touhou at. Here's the recommendations:

* Touhou 6 to 13 (fullscreen): 640x480
* Touhou 14 onward (fullscreen): 1280x960
* Game in fullscreen with WINE with fshack: Your screen's resolution, preferably cropped to 4:3 (for example: 1440x1080)

Fshack is a patch that can be added to Wine that prevents the game's resolution from affecting your display's.

## Supported encoders

### Video
* x264 (default)
* x264rgb

### Audio
* pcm (default)
* mp3
* opus

If you don't know what to choose, x264 is recommended with either pcm or mp3. As for x264, the superfast preset is reasonable in terms of speed and compression quality. Use ultrafast if your CPU can't keep up.

## Video encoding parameters

### CRF

CRF is the encoding's control rate factor. It establishes a constant quality target and the bitrate varies throughout the video according to its need for information to produce the wanted quality. Value ranges fom 0 to 51, with 0 being lossless and higher values producing lower quality.

A good value for decent quality with not-so-huge file sizes would be a CRF between 8 and 15.

### Keyframe interval

The keyframe interval determines the interval in frames between keyframes, where between them are secondary frames. These secondary frames share identical pixel data with each other.

### Color format

The color/pixel format determines how your color information is encoded. For a faithful capture of your gameplay, use rgb24 or an encoder that assumes rgb24, such as x264rgb.

Otherwise, you can use YUV. YUV is able to lower the sampling for color information, while keeping the light information intact. It is useful for lower bitrate demands and file sizes without very noticeable quality loss. YUV 444 has no sampling, 422 and 420 do.

## Audio encoding parameters

### Bit depth

Normally used by PCM in Tanuki, a higher bit depth determines a higher resolution in volume amplitude. Generally, 16bit is fine, but if you are crazy you can always pick 24bit.

### Bitrate

Audio bitrate works the same as video bitrate. Tanuki sets a constant bitrate for your audio encode. It's measured in kilobits per second, and the higher it is, the higher the audio quality, at the cost of higher file size.

<p align="center">
<img src="../images/yuuka.png" height="220"/>
</p>

