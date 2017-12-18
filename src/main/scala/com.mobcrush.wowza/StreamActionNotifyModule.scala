package com.mobcrush.wowza

import com.wowza.wms.amf.AMFPacket
import com.wowza.wms.application.{IApplicationInstance, WMSProperties}
import com.wowza.wms.logging.{WMSLogger, WMSLoggerFactory}
import com.wowza.wms.media.model.{MediaCodecInfoAudio, MediaCodecInfoVideo}
import com.wowza.wms.module.ModuleBase
import com.wowza.wms.stream.{IMediaStream, IMediaStreamActionNotify2, IMediaStreamActionNotify3}

/**
  * Created by msekerjitsky on 14.12.2017.
  */
class StreamActionNotifyModule extends ModuleBase {

  private var logger: WMSLogger = _

  def onAppStart(appInstance: IApplicationInstance): Unit = {
    logger = WMSLoggerFactory.getLoggerObj(appInstance)
    logger.info("StreamActionNotifyModule: onAppStart")
  }

  def onStreamCreate(stream: IMediaStream): Unit = {
    logger.info("onStreamCreate[" + stream + "]: clientId:" + stream.getClientId)
    val actionNotify: IMediaStreamActionNotify2 = new StreamListener

    val props: WMSProperties = stream.getProperties
    props.synchronized {
      props.setProperty("streamActionNotifier", actionNotify)
    }

    stream.addClientListener(actionNotify)
  }

  def onStreamDestroy(stream: IMediaStream): Unit = {
    logger.info("onStreamDestroy[" + stream + "]: clientId:" + stream.getClientId)

    var actionNotify: IMediaStreamActionNotify2 = null
    val props: WMSProperties = stream.getProperties
    props.synchronized {
      actionNotify = stream.getProperties.get("streamActionNotifier").asInstanceOf[IMediaStreamActionNotify2]
    }

    if (actionNotify != null) {
      stream.removeClientListener(actionNotify)
      logger.info("removeClientListener: " + stream.getSrc)
    }
  }

  private class StreamListener extends IMediaStreamActionNotify3 {

    override def onCodecInfoVideo(iMediaStream: IMediaStream, mediaCodecInfoVideo: MediaCodecInfoVideo): Unit = {
      logger.info("StreamListener: onCodecInfoVideo")
    }

    override def onCodecInfoAudio(iMediaStream: IMediaStream, mediaCodecInfoAudio: MediaCodecInfoAudio): Unit = {
      logger.info("StreamListener: onCodecInfoAudio")
    }

    override def onPauseRaw(iMediaStream: IMediaStream, b: Boolean, v: Double): Unit = {
      logger.info("StreamListener: onPauseRaw")
    }

    override def onMetaData(iMediaStream: IMediaStream, amfPacket: AMFPacket): Unit = {
      logger.info("StreamListener: onMetaData")
    }

    override def onSeek(iMediaStream: IMediaStream, v: Double): Unit = {
      logger.info("StreamListener: onSeek")
    }

    override def onPause(iMediaStream: IMediaStream, b: Boolean, v: Double): Unit = {
      logger.info("StreamListener: onPause")
    }

    override def onPlay(iMediaStream: IMediaStream, s: String, v: Double, v1: Double, i: Int): Unit = {
      logger.info("StreamListener: onPlay")
    }

    override def onPublish(iMediaStream: IMediaStream, s: String, b: Boolean, b1: Boolean): Unit = {
      logger.info("StreamListener: onPublish")
    }

    override def onUnPublish(iMediaStream: IMediaStream, s: String, b: Boolean, b1: Boolean): Unit = {
      logger.info("StreamListener: onUnPublish")
    }

    override def onStop(iMediaStream: IMediaStream): Unit = {
      logger.info("StreamListener: onStop")
    }
  }

}
