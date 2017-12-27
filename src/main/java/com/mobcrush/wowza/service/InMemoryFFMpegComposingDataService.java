package com.mobcrush.wowza.service;

import com.google.common.collect.Lists;
import com.mobcrush.wowza.model.CompositeActionModel;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.util.Asserts.notNull;

public class InMemoryFFMpegComposingDataService implements FFMpegComposingDataService {

    private List<CompositeActionModel> composingData = Lists.newArrayList();

    @Override
    public void add(@NotNull CompositeActionModel model) {
        notNull(model, "Must not be null");

        composingData.add(model);
    }

    @Nullable
    @Override
    public CompositeActionModel get(@NotNull String streamName) {
        notNull(streamName, "Must not be null");

        List<CompositeActionModel> models = composingData.stream().filter(model -> {
            return streamName.equals(model.getMasterStreamUrl())
                    || streamName.equals(model.getSlaveStreamUrl())
                    || streamName.equals(model.getTargetStreamUrl());
        }).collect(Collectors.toList());

        return models.isEmpty() ? null : models.get(0);
    }

    List<CompositeActionModel> getComposingData() {
        return composingData;
    }
}
