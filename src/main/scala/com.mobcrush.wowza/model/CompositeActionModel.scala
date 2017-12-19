package com.mobcrush.wowza.model

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

/**
  * Created by msekerjitsky on 19.12.2017.
  */
@JsonIgnoreProperties(ignoreUnknown = true)
case class CompositeActionModel(@JsonProperty("masterStreamUrl") masterStreamUrl: String,
                                @JsonProperty("slaveStreamUrl") slaveStreamUrl: String,
                                @JsonProperty("targetStreamUrl") targetStreamUrl: String)
