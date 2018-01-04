package com.mobcrush.wowza

import com.mobcrush.wowza.service.{FFMpegProcessTerminator, InMemoryFFMpegComposingDataService}
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
    }

    override def onCodecInfoAudio(iMediaStream: IMediaStream, mediaCodecInfoAudio: MediaCodecInfoAudio): Unit = {
    }

    override def onPauseRaw(iMediaStream: IMediaStream, b: Boolean, v: Double): Unit = {
      logger.info("StreamListener: onPauseRaw: " + iMediaStream.getName)
    }

    override def onMetaData(iMediaStream: IMediaStream, amfPacket: AMFPacket): Unit = {
      logger.info("StreamListener: onMetaData: " + iMediaStream.getName)
    }

    override def onSeek(iMediaStream: IMediaStream, v: Double): Unit = {
      logger.info("StreamListener: onSeek: " + iMediaStream.getName)
    }

    override def onPause(iMediaStream: IMediaStream, b: Boolean, v: Double): Unit = {
      logger.info("StreamListener: onPause: " + iMediaStream.getName)
    }

    override def onPlay(iMediaStream: IMediaStream, s: String, v: Double, v1: Double, i: Int): Unit = {
      logger.info("StreamListener: onPlay: " + iMediaStream.getName)
    }

    override def onPublish(iMediaStream: IMediaStream, s: String, b: Boolean, b1: Boolean): Unit = {
      logger.info("StreamListener: onPublish: "  + iMediaStream.getName)
    }

    override def onUnPublish(iMediaStream: IMediaStream, s: String, b: Boolean, b1: Boolean): Unit = {
      logger.info("StreamListener: onUnPublish: " + iMediaStream.getName)
      val streamName = iMediaStream.getName

      logger.info("StreamListener: onStop: " + streamName)
      val actionModel = InMemoryFFMpegComposingDataService.get(streamName)
      if (actionModel == null) {
        logger.info("Not found matching stream data")
        return
      }

      if (streamName.equals(actionModel.getMasterStreamUrl) || streamName.equals(actionModel.getSlaveStreamUrl)) {
        logger.error("Master or slave stream name match")
        FFMpegProcessTerminator.shutdown(streamName)
      } else {
        logger.error("Target stream name match")
      }
    }

    override def onStop(iMediaStream: IMediaStream): Unit = {
      logger.info("StreamListener: onStop: " + iMediaStream.getName)
    }
  }

}
