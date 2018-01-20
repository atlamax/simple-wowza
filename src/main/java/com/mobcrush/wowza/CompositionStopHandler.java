package com.mobcrush.wowza;

import com.mobcrush.wowza.service.FFMpegProcessTerminator;
import com.mobcrush.wowza.service.InMemoryFFMpegComposingDataService;
import com.wowza.wms.http.HTTPProvider2Base;
import com.wowza.wms.http.IHTTPRequest;
import com.wowza.wms.http.IHTTPResponse;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.vhost.IVHost;

public class CompositionStopHandler extends HTTPProvider2Base {

    private WMSLogger logger = WMSLoggerFactory.getLogger(this.getClass());

    private static final String STREAM_NAME_PARAMETER = "streamName";

    @Override
    public void onHTTPRequest(IVHost host, IHTTPRequest request, IHTTPResponse response) {

        logger.error("Handle incoming request to start composition");

        String streamName = request.getParameter(STREAM_NAME_PARAMETER);
        if (streamName == null || streamName.isEmpty()) {
            logger.error("CompositionStopHandler: Empty 'streamName' query parameter");
            return;
        }

        logger.error("CompositionStopHandler: streamName: '" + streamName + "'");

        FFMpegProcessTerminator.shutdown(streamName);
        InMemoryFFMpegComposingDataService.remove(streamName);
    }
}
