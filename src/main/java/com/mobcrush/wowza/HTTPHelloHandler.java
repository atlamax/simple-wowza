package com.mobcrush.wowza;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.wowza.model.CompositeActionModel;
import com.mobcrush.wowza.service.FFMpegService;
import com.mobcrush.wowza.service.InMemoryFFMpegComposingDataService;
import com.wowza.wms.http.HTTPProvider2Base;
import com.wowza.wms.http.IHTTPRequest;
import com.wowza.wms.http.IHTTPResponse;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.stream.MediaStreamMap;
import com.wowza.wms.vhost.IVHost;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPHelloHandler extends HTTPProvider2Base {

    private static final String APPLICATION_NAME = "live";
    private static final String APPLICATION_INSTANCE_NAME = "_definst_";

    private WMSLogger logger = WMSLoggerFactory.getLogger(this.getClass());

    private ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onHTTPRequest(IVHost host, IHTTPRequest request, IHTTPResponse response) {

        if (!validateRequest(request, response)) {
            return;
        }

        String requestBody = null;
        try {
            requestBody = IOUtils.toString(request.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            logger.error("Error occur during reading input stream", e);
        }

        if (requestBody == null) {
            response.setResponseCode(HttpStatus.SC_BAD_REQUEST);
            return;
        }

        CompositeActionModel model = parseRequestBody(requestBody);
        if (model == null) {
            response.setResponseCode(HttpStatus.SC_BAD_REQUEST);
            return;
        }

        if (!validateStreamNames(host, response, model)) {
            return;
        }

        response.setResponseCode(HttpStatus.SC_OK);
        FFMpegService.run(model);
        storeStreamingContext(model);
    }

    private CompositeActionModel parseRequestBody(String requestBody) {

        CompositeActionModel result = null;

        try {
            result = MAPPER.readValue(requestBody, CompositeActionModel.class);
        } catch (Exception e) {
            logger.error("Error occur during parsing", e);
        }

        return result;
    }

    private boolean validateRequest(IHTTPRequest request, IHTTPResponse response) {
        boolean isValid = true;

        if (!HttpPost.METHOD_NAME.equalsIgnoreCase(request.getMethod())) {
            isValid = false;
            response.setResponseCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
        }

        return isValid;
    }

    private boolean validateStreamNames(IVHost host, IHTTPResponse response, CompositeActionModel model) {

        MediaStreamMap streams = host.getApplication(APPLICATION_NAME).getAppInstance(APPLICATION_INSTANCE_NAME)
                .getStreams();
        List<String> streamsUrl = streams.getStreams().stream()
                .map(stream -> stream.getClient().getUri() + "/" + stream.getName())
                .collect(Collectors.toList());

        if (!streamsUrl.contains(model.getMasterStreamUrl())
                || !streamsUrl.contains(model.getSlaveStreamUrl())
                || streamsUrl.contains(model.getTargetStreamUrl())
        ) {
            response.setResponseCode(HttpStatus.SC_BAD_REQUEST);
            return false;
        }

        return true;
    }

    private void storeStreamingContext(CompositeActionModel actionModel) {
        actionModel.setMasterStreamUrl(parseStreamName(actionModel.getMasterStreamUrl()));
        actionModel.setSlaveStreamUrl(parseStreamName(actionModel.getSlaveStreamUrl()));
        actionModel.setTargetStreamUrl(parseStreamName(actionModel.getTargetStreamUrl()));

        InMemoryFFMpegComposingDataService.add(actionModel);
    }

    /**
     * Parse stream name from it's full URL
     *
     * @param streamUrl stream URL to parse
     *
     * @return stream name
     */
    private String parseStreamName(String streamUrl) {
        String[] splittedUrl = streamUrl.split("/");

        int streamNameIndex = splittedUrl.length - 1;
        if (splittedUrl[streamNameIndex].isEmpty() && streamNameIndex > 0) {
            streamNameIndex--;
        }

        logger.error("Parssed name: " + splittedUrl[streamNameIndex]);
        return splittedUrl[streamNameIndex];
    }
}
