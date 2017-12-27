package com.mobcrush.wowza.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    /****************************************
     ********** EQUALS & HASHCODE ***********
     ****************************************/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CompositeActionModel that = (CompositeActionModel) o;

        return new EqualsBuilder()
                .append(masterStreamUrl, that.masterStreamUrl)
                .append(slaveStreamUrl, that.slaveStreamUrl)
                .append(targetStreamUrl, that.targetStreamUrl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(masterStreamUrl)
                .append(slaveStreamUrl)
                .append(targetStreamUrl)
                .toHashCode();
    }
}
