package com.mobcrush.wowza.service;

import com.mobcrush.wowza.model.StreamData;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Service to access stream data model
 */
public class StreamDataService {

    private static ConcurrentHashMap<String, StreamData> STREAMS_DATA = new ConcurrentHashMap<>();

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
            return STREAMS_DATA.get(streamName);
        }

        StreamData result = new StreamData();
        STREAMS_DATA.put(streamName, result);

        return result;
    }

    /**
     * Remove data about stream
     *
     * @param streamName stream name
     */
    public static void remove(@Nonnull String streamName) {
        notNull(streamName, "Stream name must not be null");

        STREAMS_DATA.remove(streamName);
    }
}
