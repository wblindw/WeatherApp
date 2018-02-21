
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Value",
    "Unit",
    "UnitType"
})
public class Snow {

    @JsonProperty("Value")
    public Integer value;
    @JsonProperty("Unit")
    public String unit;
    @JsonProperty("UnitType")
    public Integer unitType;

}
