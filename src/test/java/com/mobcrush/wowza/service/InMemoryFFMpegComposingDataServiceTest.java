package com.mobcrush.wowza.service;

import com.mobcrush.wowza.model.CompositeActionModel;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryFFMpegComposingDataServiceTest {

    private InMemoryFFMpegComposingDataService sut;

    @Before
    public void setUp() {
        sut = new InMemoryFFMpegComposingDataService();
    }

    @Test
    public void testGetReturnModelWithMatchingOnMasterStreamName() {
        // given
        String streamName = "master";
        CompositeActionModel model = new CompositeActionModel();
        model.setMasterStreamUrl(streamName);

        sut.getComposingData().add(model);
        sut.getComposingData().add(new CompositeActionModel());
        // when
        CompositeActionModel result = sut.get(streamName);
        // then
        assertNotNull(result);
        assertEquals(model, result);
    }

    @Test
    public void testGetReturnModelWithMatchingOnSlaveStreamName() {
        // given
        String streamName = "slave";
        CompositeActionModel model = new CompositeActionModel();
        model.setSlaveStreamUrl(streamName);

        sut.getComposingData().add(model);
        sut.getComposingData().add(new CompositeActionModel());
        // when
        CompositeActionModel result = sut.get(streamName);
        // then
        assertNotNull(result);
        assertEquals(model, result);
    }

    @Test
    public void testGetReturnModelWithMatchingOnTargetStreamName() {
        // given
        String streamName = "target";
        CompositeActionModel model = new CompositeActionModel();
        model.setTargetStreamUrl(streamName);

        sut.getComposingData().add(model);
        sut.getComposingData().add(new CompositeActionModel());
        // when
        CompositeActionModel result = sut.get(streamName);
        // then
        assertNotNull(result);
        assertEquals(model, result);
    }

    @Test
    public void testGetReturnNullForUnknownName() {
        // given
        String streamName = "any";
        CompositeActionModel model = new CompositeActionModel();
        model.setMasterStreamUrl("master");
        model.setSlaveStreamUrl("slave");
        model.setTargetStreamUrl("target");

        sut.getComposingData().add(model);
        sut.getComposingData().add(new CompositeActionModel());
        // when
        CompositeActionModel result = sut.get(streamName);
        // then
        assertNull(result);
    }

    @Test
    public void test() {
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("master")
                .addInput("slave")
                .setComplexFilter("comple filter")
                .addOutput("target")
                    .addExtraArgs("-map", "[vid]")
                    .addExtraArgs("-report")
                    .addExtraArgs("-loglevel", "verbose")
                    .addExtraArgs("mono_audio")
                    .setVideoCodec("libx264")
                    .setConstantRateFactor(23)
                    .setPreset("veryfast")
                    .setFormat("flv")
                    .done();

        assertNotNull(builder);
    }
}
