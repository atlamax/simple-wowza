package com.mobcrush.wowza.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Model with stream data
 */
public class StreamData {

    private int audioChannelsNumber = 0;

    public int getAudioChannelsNumber() {
        return audioChannelsNumber;
    }

    public void setAudioChannelsNumber(int audioChannelsNumber) {
        this.audioChannelsNumber = audioChannelsNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
