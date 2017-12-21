package com.mobcrush.wowza;

import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

public class FFMpegProgressListener implements ProgressListener {

    private static final WMSLogger LOGGER = WMSLoggerFactory.getLogger(FFMpegProgressListener.class);

    @Override
    public void progress(Progress progress) {
        LOGGER.info("FFMpeg progress: " + progress.toString());
    }
}
