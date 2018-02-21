
package com.blind.wakemeup.weather.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lon",
    "lat"
})
public class Coord implements Serializable
{

    @JsonProperty("lon")
    public Float lon;
    @JsonProperty("lat")
    public Float lat;
    private final static long serialVersionUID = -585224939623808176L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lon", lon).append("lat", lat).toString();
    }

}
