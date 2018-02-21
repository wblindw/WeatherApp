
package com.blind.wakemeup.weather.accu.model.day;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Past6HourRange",
    "Past12HourRange",
    "Past24HourRange"
})
public class TemperatureSummary {

    @JsonProperty("Past6HourRange")
    public Past6HourRange past6HourRange;
    @JsonProperty("Past12HourRange")
    public Past12HourRange past12HourRange;
    @JsonProperty("Past24HourRange")
    public Past24HourRange past24HourRange;

}
