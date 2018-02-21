
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Heating",
    "Cooling"
})
public class DegreeDaySummary {

    @JsonProperty("Heating")
    public Heating heating;
    @JsonProperty("Cooling")
    public Cooling cooling;

}
