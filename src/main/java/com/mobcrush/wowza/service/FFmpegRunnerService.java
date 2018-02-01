package com.mobcrush.wowza.service;

import com.mobcrush.wowza.builder.FFmpegJobFactory;
import com.mobcrush.wowza.model.CompositeActionModel;
import com.mobcrush.wowza.model.StreamData;
import com.mobcrush.wowza.parser.StreamNameParser;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.Validate.notNull;

public class FFmpegRunnerService {

    private static WMSLogger LOGGER = WMSLoggerFactory.getLogger(FFmpegRunnerService.class);

    private static final String COMPLEX_FILTER_BASE = "[0:v]crop=iw:ih/2:0:ih/4[top];[1:v]crop=iw:ih/2:0:ih/4[bottom];[top][bottom]vstack[vid]";
    private static final String STEREO_AUDIO_COMPLEX_FILTER = ";amerge,pan=stereo|c0<c0|c1<c1";
    private static final String MONO_AUDIO_COMPLEX_FILTER = ";[0:a][0:a]amerge=inputs=2[aout]";
    private static final String[] MONO_AUDIO_MAP_PARAMETERS = new String[] {"-map", "[aout]"};

    public static void run(@Nonnull CompositeActionModel actionModel) {
        notNull(actionModel, "Action model must not be null");

        StreamData streamData = StreamDataService.get(
                StreamNameParser.parseFromFullURL(actionModel.getMasterStreamUrl())
        );
        LOGGER.error("Stream data to run FFmpeg: " + streamData);

        try {
            FFmpeg ffmpeg = new FFmpeg();
            FFprobe ffprobe = new FFprobe();
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(actionModel.getMasterStreamUrl())
                    .addInput(actionModel.getSlaveStreamUrl())
                    .addExtraArgs("-threads", "0")
                    .setComplexFilter(COMPLEX_FILTER_BASE + MONO_AUDIO_COMPLEX_FILTER)
                    .setVerbosity(FFmpegBuilder.Verbosity.VERBOSE)
                    .addOutput(actionModel.getTargetStreamUrl())
                        .addExtraArgs("-map", "[vid]")
//                        .addExtraArgs("-report")
                        .addExtraArgs(MONO_AUDIO_MAP_PARAMETERS)
                        .addExtraArgs("-g", "2")
                        .addExtraArgs("-r", "30")
                        .setVideoCodec("libx264")
                        .setConstantRateFactor(30)
                        .setPreset("veryfast")
                        .setFormat("flv")
                        .done();

//            setComplexFilter(builder, streamData);
//            addExtraArgs(builder, streamData);

            FFmpegJob job = new FFmpegExecutor(ffmpeg, ffprobe).createJob(builder);
            new Thread(() -> job.run()).start();
        } catch (Exception e) {
            LOGGER.error("Error occur during running FFMpeg", e);
        }
    }

    private static void setComplexFilter(FFmpegBuilder builder, StreamData streamData) {
        String audioFilter = isMonoAudio(streamData)
                ? MONO_AUDIO_COMPLEX_FILTER
                : STEREO_AUDIO_COMPLEX_FILTER;

        builder.setComplexFilter(COMPLEX_FILTER_BASE + audioFilter);
    }

    private static void addExtraArgs(FFmpegBuilder builder, StreamData streamData) {
        builder.addExtraArgs("-map", "[vid]")
                .addExtraArgs("-report")
                .addExtraArgs("-loglevel", "verbose");

        if (isMonoAudio(streamData)) {
            builder.addExtraArgs(MONO_AUDIO_MAP_PARAMETERS);
        }
    }

    private static boolean isMonoAudio(StreamData streamData) {
        return streamData.getAudioChannelsNumber() == 1;
    }
}
