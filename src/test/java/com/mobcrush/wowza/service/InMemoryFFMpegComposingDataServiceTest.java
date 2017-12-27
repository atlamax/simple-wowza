package com.mobcrush.wowza.service;

import com.mobcrush.wowza.model.CompositeActionModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        // when
        CompositeActionModel result = sut.get(streamName);
        // then
        assertNotNull(result);
        assertEquals(model, result);
    }
}
