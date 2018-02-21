
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Rise",
    "EpochRise",
    "Set",
    "EpochSet"
})
public class Sun {

    @JsonProperty("Rise")
    public String rise;
    @JsonProperty("EpochRise")
    public Integer epochRise;
    @JsonProperty("Set")
    public String set;
    @JsonProperty("EpochSet")
    public Integer epochSet;

}
