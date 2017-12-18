package com.mobcrush.wowza

import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.bramp.ffmpeg.builder.FFmpegBuilder.Verbosity
import net.bramp.ffmpeg.{FFmpeg, FFmpegExecutor}

class FFMpegRunner {

  def run(): Unit = {
    try {
      val ffmpeg: FFmpeg = new FFmpeg()
      val builder: FFmpegBuilder = new FFmpegBuilder()
        .setVerbosity(Verbosity.INFO)
        .setInput(sys.props.get("user.home").get + sys.props.get("file.separator").get + "despacito.mp4")
        .addInput("rtmp://172.18.17.90:1935/live/myStream")
        .setComplexFilter("[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]")
        .addOutput("rtmp://172.18.17.90:1935/live/myStream1")
          .addExtraArgs("-map", "[vid]")
          .setVideoCodec("libx264")
          .setConstantRateFactor(23)
          .setPreset("veryfast")
          .setFormat("flv")
          .done()

      new FFmpegExecutor().createJob(builder).run()
    } catch {
      case e: RuntimeException => {
        e.printStackTrace()
      }
    }

  }

}
