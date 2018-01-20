package com.mobcrush.wowza.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.mobcrush.wowza.model.CompositeActionModel;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notNull;


public class InMemoryFFMpegComposingDataService {

    private static WMSLogger logger = WMSLoggerFactory.getLogger(InMemoryFFMpegComposingDataService.class);
    private static List<CompositeActionModel> composingData = Lists.newArrayList();

    public static void add(@Nonnull CompositeActionModel model) {
        notNull(model, "Must not be null");

        logger.error("Store in context master stream name: " + model.getMasterStreamUrl());
        logger.error("Store in context slave stream name: " + model.getSlaveStreamUrl());
        logger.error("Store in context target stream name: " + model.getTargetStreamUrl());
        composingData.add(model);
    }

    @Nullable
    public static CompositeActionModel get(@Nonnull String streamName) {
        notNull(streamName, "Stream name must not be null");

        logger.error("Trying to get streaming context by name: " + streamName);

        List<CompositeActionModel> models = composingData.stream().filter(model -> {
            return streamName.equals(model.getMasterStreamUrl())
                    || streamName.equals(model.getSlaveStreamUrl())
                    || streamName.equals(model.getTargetStreamUrl());
        }).collect(Collectors.toList());

        return models.isEmpty() ? null : models.get(0);
    }

    public static void remove(@Nonnull String targetStreamName) {
        notNull(targetStreamName, "Target stream name must not be null");

        logger.error("Trying to remove streaming context by name: " + targetStreamName);

        List<CompositeActionModel> models = composingData.stream().filter(model -> {
            return targetStreamName.equals(model.getTargetStreamUrl());
        }).collect(Collectors.toList());

        if (models.size() == 1) {
            composingData.remove(models.get(0));
        }
    }

    @VisibleForTesting
    List<CompositeActionModel> getComposingData() {
        return composingData;
    }
}
