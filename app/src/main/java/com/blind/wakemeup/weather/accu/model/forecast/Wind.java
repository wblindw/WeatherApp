
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Speed",
    "Direction"
})
public class Wind {

    @JsonProperty("Speed")
    public Speed speed;
    @JsonProperty("Direction")
    public Direction direction;

}
