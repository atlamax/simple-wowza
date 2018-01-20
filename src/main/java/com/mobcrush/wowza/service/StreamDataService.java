package com.mobcrush.wowza.service;

import com.mobcrush.wowza.model.StreamData;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Service to access stream data model
 */
public class StreamDataService {

    private static WMSLogger LOGGER = WMSLoggerFactory.getLogger(StreamDataService.class);

    private static ConcurrentHashMap<String, StreamData> STREAMS_DATA = new ConcurrentHashMap<>();

    /**
     * Get stream data
     *
     * @param streamName stream name
     *
     * @return stream data
     */
    @Nullable
    public static StreamData get(@Nonnull String streamName) {
        notNull(streamName, "Stream name must not be null");

        LOGGER.error("StreamDataService.get: " + streamName);

        return STREAMS_DATA.get(streamName);
    }

    /**
     * Get stream data if it's already exists, or create new and associate with stream name
     *
     * @param streamName stream name
     *
     * @return stream data
     */
    @Nonnull
    public static StreamData getOrCreate(@Nonnull String streamName) {
        notNull(streamName, "Stream name must not be null");

        if (STREAMS_DATA.containsKey(streamName)) {
            LOGGER.error("StreamDataService.getOrCreate found: " + streamName);
            return STREAMS_DATA.get(streamName);
        }

        StreamData result = new StreamData();
        STREAMS_DATA.put(streamName, result);
        LOGGER.error("StreamDataService.getOrCreate create new: " + streamName);

        return result;
    }

    /**
     * Remove data about stream
     *
     * @param streamName stream name
     */
    public static void remove(@Nonnull String streamName) {
        notNull(streamName, "Stream name must not be null");

        LOGGER.error("StreamDataService.remove: " + streamName);

        STREAMS_DATA.remove(streamName);
    }
}
