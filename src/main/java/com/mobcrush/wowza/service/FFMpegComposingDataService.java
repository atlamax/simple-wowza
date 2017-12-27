package com.mobcrush.wowza.service;

import com.mobcrush.wowza.model.CompositeActionModel;

public interface FFMpegComposingDataService {

    void add(CompositeActionModel model);

    CompositeActionModel get(String streamName);
}
