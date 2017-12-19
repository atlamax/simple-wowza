package com.mobcrush.wowza

import com.mobcrush.wowza.model.CompositeActionModel
import com.wowza.wms.logging.{WMSLogger, WMSLoggerFactory}
import net.bramp.ffmpeg.builder.FFmpegBuilder
import net.bramp.ffmpeg.builder.FFmpegBuilder.Verbosity
import net.bramp.ffmpeg.{FFmpeg, FFmpegExecutor}

object FFMpegRunner {

  private val LOGGER: WMSLogger = WMSLoggerFactory.getLogger(this.getClass)

  def run(actionModel: CompositeActionModel): Unit = {

    try {
      val ffmpeg: FFmpeg = new FFmpeg()
      val builder: FFmpegBuilder = new FFmpegBuilder()
        .setVerbosity(Verbosity.INFO)
        .setInput(sys.props.get("user.home").get + sys.props.get("file.separator").get + "despacito.mp4")
//        .addInput("rtmp://172.18.17.90:1935/live/myStream")
        .addInput(actionModel.slaveStreamUrl)
        .setComplexFilter("[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]")
//        .addOutput("rtmp://172.18.17.90:1935/live/myStream1")
        .addOutput(actionModel.targetStreamUrl)
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
