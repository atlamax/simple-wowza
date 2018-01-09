package com.mobcrush.wowza;

import com.mobcrush.wowza.model.CompositeActionModel;
import com.mobcrush.wowza.service.FFMpegProcessTerminator;
import com.mobcrush.wowza.service.InMemoryFFMpegComposingDataService;
import com.wowza.wms.amf.AMFPacket;
import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.application.WMSProperties;
import com.wowza.wms.media.model.MediaCodecInfoAudio;
import com.wowza.wms.media.model.MediaCodecInfoVideo;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.IMediaStream;
import com.wowza.wms.stream.IMediaStreamActionNotify3;

public class StreamActionNotifyModule extends ModuleBase {

    public void onAppStart(IApplicationInstance appInstance) {
        getLogger().info("StreamActionNotifyModule: onAppStart");
    }

    public void  onStreamCreate(IMediaStream stream ) {
        getLogger().info("onStreamCreate[" + stream + "]: clientId:" + stream.getClientId());
        StreamListener streamListener = new StreamListener();

        WMSProperties props = stream.getProperties();
        synchronized (props) {
            props.setProperty("streamActionNotifier", streamListener);
        }

        stream.addClientListener(streamListener);
    }

    public void onStreamDestroy(IMediaStream stream) {
        getLogger().info("onStreamDestroy[" + stream + "]: clientId:" + stream.getClientId());

        IMediaStreamActionNotify3 streamListener;
        WMSProperties props = stream.getProperties();
        synchronized (props) {
            streamListener = (IMediaStreamActionNotify3) stream.getProperties().get("streamActionNotifier");
        }

        if (streamListener != null) {
            stream.removeClientListener(streamListener);
            getLogger().info("removeClientListener: " + stream.getSrc());
        }
    }

    class StreamListener implements IMediaStreamActionNotify3 {

        @Override
        public void onCodecInfoVideo(IMediaStream iMediaStream, MediaCodecInfoVideo mediaCodecInfoVideo) {
//            getLogger().error("StreamListener: onCodecInfoVideo: " + iMediaStream.getName());
        }

        @Override
        public void onCodecInfoAudio(IMediaStream iMediaStream, MediaCodecInfoAudio mediaCodecInfoAudio) {
            getLogger().error("StreamListener: onCodecInfoAudio: " + iMediaStream.getName());
        }

        @Override
        public void onPauseRaw(IMediaStream iMediaStream, boolean b, double v) {
            getLogger().info("StreamListener: onPauseRaw: " + iMediaStream.getName());
        }

        @Override
        public void onMetaData(IMediaStream iMediaStream, AMFPacket amfPacket) {
            getLogger().info("StreamListener: onMetaData: " + iMediaStream.getName());
        }

        @Override
        public void onSeek(IMediaStream iMediaStream, double v) {
            getLogger().info("StreamListener: onSeek: " + iMediaStream.getName());
        }

        @Override
        public void onPause(IMediaStream iMediaStream, boolean b, double v) {
            getLogger().info("StreamListener: onPause: " + iMediaStream.getName());
        }

        @Override
        public void onPlay(IMediaStream iMediaStream, String s, double v, double vl, int i) {
            getLogger().info("StreamListener: onPlay: " + iMediaStream.getName());
        }

        @Override
        public void onPublish(IMediaStream iMediaStream, String s, boolean b, boolean bl) {
            getLogger().info("StreamListener: onPublish: " + iMediaStream.getName());
        }

        @Override
        public void onUnPublish(IMediaStream iMediaStream, String s, boolean b, boolean bl) {
            getLogger().info("StreamListener: onUnPublish: " + iMediaStream.getName());
            String streamName = iMediaStream.getName();

            CompositeActionModel actionModel = InMemoryFFMpegComposingDataService.get(streamName);
            if (actionModel == null) {
                getLogger().error("Not found matching stream data");
                return;
            }

            if (streamName.equals(actionModel.getMasterStreamUrl()) || streamName.equals(actionModel.getSlaveStreamUrl())) {
                getLogger().error("Master or slave stream name match");
                FFMpegProcessTerminator.shutdown(actionModel.getTargetStreamUrl());
                InMemoryFFMpegComposingDataService.remove(actionModel.getTargetStreamUrl());
            } else {
                getLogger().error("Target stream name match");
            }
        }

        @Override
        public void onStop(IMediaStream iMediaStream) {
            getLogger().info("StreamListener: onStop: " + iMediaStream.getName());
        }
    }
}
