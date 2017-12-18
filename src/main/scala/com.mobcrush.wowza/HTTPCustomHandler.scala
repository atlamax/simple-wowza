package com.mobcrush.wowza

import com.wowza.wms.http.{HTTPProvider2Base, IHTTPRequest, IHTTPResponse}
import com.wowza.wms.vhost.IVHost

/**
  * Created by msekerjitsky on 15.12.2017.
  */
class HTTPCustomHandler extends HTTPProvider2Base {

  override def onHTTPRequest(ivHost: IVHost, ihttpRequest: IHTTPRequest, ihttpResponse: IHTTPResponse): Unit = {
    ihttpResponse.setResponseCode(200)
    ihttpResponse.getOutputStream.write("Hello, Wowza!".getBytes)
  }
}
