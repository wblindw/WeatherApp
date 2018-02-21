
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Rise",
    "EpochRise",
    "Set",
    "EpochSet",
    "Phase",
    "Age"
})
public class Moon {

    @JsonProperty("Rise")
    public Object rise;
    @JsonProperty("EpochRise")
    public Object epochRise;
    @JsonProperty("Set")
    public String set;
    @JsonProperty("EpochSet")
    public Integer epochSet;
    @JsonProperty("Phase")
    public String phase;
    @JsonProperty("Age")
    public Integer age;

}
