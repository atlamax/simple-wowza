package com.mobcrush.wowza

import java.nio.charset.Charset

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.mobcrush.wowza.model.CompositeActionModel
import com.wowza.wms.http.{HTTPProvider2Base, IHTTPRequest, IHTTPResponse}
import com.wowza.wms.logging.{WMSLogger, WMSLoggerFactory}
import com.wowza.wms.vhost.IVHost
import org.apache.commons.io.IOUtils

/**
  * Created by msekerjitsky on 15.12.2017.
  */
class HTTPCustomHandler extends HTTPProvider2Base {

  private val logger: WMSLogger = WMSLoggerFactory.getLogger(this.getClass)

  private val MAPPER: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  override def onHTTPRequest(host: IVHost, request: IHTTPRequest, response: IHTTPResponse): Unit = {
    response.setResponseCode(200)
    response.getOutputStream.write("Scala, Wowza!".getBytes)
    if (!validateRequest(request, response)) {
      return
    }

    val requestBody = IOUtils.toString(request.getInputStream, Charset.forName("UTF-8"))
    parseRequestBody(requestBody) match {
      case Some(actionModel) => {
        response.setResponseCode(200)
        FFMpegRunner.run(actionModel)
      }
      case None => {
        response.setResponseCode(400)
      }
    }
  }

  private def parseRequestBody(requestBody: String): Option[CompositeActionModel] = {

    try {
      Some(
        MAPPER.readValue(requestBody, classOf[CompositeActionModel])
      )
    } catch {
      case e: Exception => {
        logger.error("Error occurred during parsing response", e)
        None
      }
    }
  }

  private def validateRequest(request: IHTTPRequest, response: IHTTPResponse): Boolean = {
    var isValid: Boolean = true

    if (!request.getMethod.equalsIgnoreCase("post")) {
      isValid = false
      response.setResponseCode(405)
    }

    isValid
  }
}
