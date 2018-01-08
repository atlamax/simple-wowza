package com.mobcrush.wowza.service;

import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;

/**
 * Created by msekerjitsky on 04.01.2018.
 */
public class FFMpegProcessTerminator {

    private static WMSLogger logger = WMSLoggerFactory.getLogger(FFMpegProcessTerminator.class);

    private static final String STREAM_NAME_PATTERN = "%STREAM_NAME_PATTERN%";
    private static final String COMMAND_TEMPLATE = "pgrep -xf ^ffmpeg.*" + STREAM_NAME_PATTERN + "$ | xargs kill -9";

    public static void shutdown(String targetStreamName) {
        String command = COMMAND_TEMPLATE.replaceFirst(STREAM_NAME_PATTERN, targetStreamName);
        logger.error("Final command: " + command);

        try {
            String[] commandsArray = {
                    "/bin/sh",
                    "-c",
                    command
            };

            Process process = Runtime.getRuntime().exec(commandsArray);

            int status = process.waitFor();
            logger.error("Command status: " + status);
        } catch (Exception e) {
            logger.error("Error occur: ", e);
        }
    }
}
