
package com.blind.wakemeup.weather.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "speed",
    "deg",
    "gust"
})
public class Wind implements Serializable
{

    @JsonProperty("speed")
    public Float speed;
    @JsonProperty("deg")
    public Float deg;
    @JsonProperty("gust")
    public Float gust;

    private final static long serialVersionUID = 2791587738090873967L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("speed", speed).append("deg", deg).append("gust", gust).toString();
    }

}
