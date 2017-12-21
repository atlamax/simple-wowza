package com.mobcrush.wowza;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.wowza.model.CompositeActionModel;
import com.wowza.wms.http.HTTPProvider2Base;
import com.wowza.wms.http.IHTTPRequest;
import com.wowza.wms.http.IHTTPResponse;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.vhost.IVHost;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class HTTPHelloHandler extends HTTPProvider2Base {

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
            response.setResponseCode(400);
            return;
        }

        CompositeActionModel model = parseRequestBody(requestBody);
        if (model == null) {
            response.setResponseCode(400);
            return;
        }

        response.setResponseCode(200);
        FFMpegService.run(model);
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

    if (!request.getMethod().equalsIgnoreCase("post")) {
        isValid = false;
        response.setResponseCode(405);
    }

    return isValid;
  }
}
