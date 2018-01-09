package com.mobcrush.wowza.service;

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

    private static final String STEREO_AUDIO_COMPLEX_FILTER = ";amerge,pan=stereo|c0<c0|c1<c1";
    private static final String MONO_AUDIO_COMPLEX_FILTER = ";[0:a][0:a]amerge=inputs=2[aout]";
    private static final String MONO_AUDIO_MAP_PARAMETER = "-map [aout]";

    public static void run(CompositeActionModel actionModel) {

        try {
            FFmpeg ffmpeg = new FFmpeg();
            FFprobe ffprobe = new FFprobe();
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(actionModel.getMasterStreamUrl())
                    .addInput(actionModel.getSlaveStreamUrl())
                    .setComplexFilter("[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]" + STEREO_AUDIO_COMPLEX_FILTER)
                    .addOutput(actionModel.getTargetStreamUrl())
                        .addExtraArgs("-map", "[vid]")
                        .setVideoCodec("libx264")
                        .setConstantRateFactor(23)
                        .setPreset("veryfast")
                        .setFormat("flv")
                        .addExtraArgs("-abort_on", "empty_output")
                        .done();

            FFmpegJob job = new FFmpegExecutor(ffmpeg, ffprobe).createJob(builder);
            new Thread(() -> job.run()).start();
        } catch (Exception e) {
            LOGGER.error("Error occur during running FFMpeg", e);
        }
    }
}
