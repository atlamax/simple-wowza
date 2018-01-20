package com.mobcrush.wowza.parser;

/**
 * Class to parse stream name
 */
public class StreamNameParser {

    private static final String URL_PARTS_DELIMITER = "/";

    /**
     * Parse stream name from it's full URL
     *
     * @param streamUrl stream URL to parse
     *
     * @return stream name
     */
    public static String parseFromFullURL(String streamUrl) {
        String[] urlParts = streamUrl.split(URL_PARTS_DELIMITER);

        int streamNameIndex = urlParts.length - 1;
        if (urlParts[streamNameIndex].isEmpty() && streamNameIndex > 0) {
            streamNameIndex--;
        }

        return urlParts[streamNameIndex];
    }
}
