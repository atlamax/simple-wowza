package com.mobcrush.wowza;

import com.wowza.wms.http.HTTPProvider2Base;
import com.wowza.wms.http.IHTTPRequest;
import com.wowza.wms.http.IHTTPResponse;
import com.wowza.wms.vhost.IVHost;

import java.io.IOException;

public class HTTPHelloHandler extends HTTPProvider2Base {

    @Override
    public void onHTTPRequest(IVHost ivHost, IHTTPRequest ihttpRequest, IHTTPResponse ihttpResponse) {

        ihttpResponse.setResponseCode(200);

        try {
            ihttpResponse.getOutputStream().write("Hello, Wowza!".getBytes());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
