package com.mobcrush.wowza.service;

import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Created by msekerjitsky on 04.01.2018.
 */
public class FFMpegProcessTerminator {

    private static WMSLogger LOGGER = WMSLoggerFactory.getLogger(FFMpegProcessTerminator.class);

    private static final String STREAM_NAME_PATTERN = "%STREAM_NAME_PATTERN%";
    private static final String COMMAND_TEMPLATE = "pgrep -xf ^ffmpeg.*" + STREAM_NAME_PATTERN + "$ | xargs kill -9";

    public static void shutdown(@Nonnull String targetStreamName) {
        notNull(targetStreamName, "Target stream name must not be null");

        String command = COMMAND_TEMPLATE.replaceFirst(STREAM_NAME_PATTERN, targetStreamName);
        LOGGER.error("Final command: " + command);

        try {
            String[] commandsArray = {
                    "/bin/sh",
                    "-c",
                    command
            };

            Process process = Runtime.getRuntime().exec(commandsArray);

            int status = process.waitFor();
            LOGGER.error("Command status: " + status);
        } catch (Exception e) {
            LOGGER.error("Error occur: ", e);
        }
    }
}
