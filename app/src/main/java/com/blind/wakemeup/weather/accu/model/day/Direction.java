
package com.blind.wakemeup.weather.accu.model.day;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Degrees",
    "Localized",
    "English"
})
public class Direction {

    @JsonProperty("Degrees")
    public Integer degrees;
    @JsonProperty("Localized")
    public String localized;
    @JsonProperty("English")
    public String english;

}
