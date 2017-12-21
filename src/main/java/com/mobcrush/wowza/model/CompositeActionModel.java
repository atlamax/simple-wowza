package com.mobcrush.wowza.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompositeActionModel {

    @JsonProperty("masterStreamUrl")
    private String masterStreamUrl;
    @JsonProperty("slaveStreamUrl")
    private String slaveStreamUrl;
    @JsonProperty("targetStreamUrl")
    private String targetStreamUrl;

    /****************************************
     ********** GETTERS & SETTERS ***********
     ****************************************/

    public String getMasterStreamUrl() {
        return masterStreamUrl;
    }

    public void setMasterStreamUrl(String masterStreamUrl) {
        this.masterStreamUrl = masterStreamUrl;
    }

    public String getSlaveStreamUrl() {
        return slaveStreamUrl;
    }

    public void setSlaveStreamUrl(String slaveStreamUrl) {
        this.slaveStreamUrl = slaveStreamUrl;
    }

    public String getTargetStreamUrl() {
        return targetStreamUrl;
    }

    public void setTargetStreamUrl(String targetStreamUrl) {
        this.targetStreamUrl = targetStreamUrl;
    }
}
