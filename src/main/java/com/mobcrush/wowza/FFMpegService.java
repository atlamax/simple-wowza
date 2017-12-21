package com.mobcrush.wowza;

import com.mobcrush.wowza.model.CompositeActionModel;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;

public class FFMpegService {

    private static WMSLogger LOGGER = WMSLoggerFactory.getLogger(FFMpegService.class);

    public static void run(CompositeActionModel actionModel) {

//        LOGGER.error("Path:" + System.getProperties().getProperty("user.home") + System.getProperties().getProperty("file.separator") + "despacito.mp4");

        try {
            FFmpeg ffmpeg = new FFmpeg();
            FFprobe ffprobe = new FFprobe();
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setVerbosity(FFmpegBuilder.Verbosity.DEBUG)
                    .setInput("c:\\Users\\msekerjitsky\\despacito.mp4")
                    .addInput(actionModel.getSlaveStreamUrl())
                    .setComplexFilter("[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]")
                    .addOutput(actionModel.getTargetStreamUrl())
                    .addExtraArgs("-map", "[vid]")
                    .setVideoCodec("libx264")
                    .setConstantRateFactor(23)
                    .setPreset("veryfast")
                    .setFormat("flv")
                    .done();

            FFmpegJob job = new FFmpegExecutor(ffmpeg, ffprobe).createJob(builder, new FFMpegProgressListener());
            new Thread(() -> job.run()).start();
        } catch (Exception e) {
            LOGGER.error("Error occur during running FFMpeg", e);
        }
    }
}
