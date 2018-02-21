
package com.blind.wakemeup.weather.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cod",
    "message",
    "cnt",
    "list",
    "city"
})
public class WeatherForcast implements Serializable
{
    @JsonProperty("city")
    public City city;
    @JsonProperty("cod")
    public String cod;
    @JsonProperty("message")
    public Float message;
    @JsonProperty("cnt")
    public Integer cnt;
    @JsonProperty("list")
    public java.util.List<com.blind.wakemeup.weather.model.forecast.List> list = new ArrayList<com.blind.wakemeup.weather.model.forecast.List>();
    private final static long serialVersionUID = 5432354435853385627L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cod", cod).append("message", message).append("city", city).append("cnt", cnt).append("list", list).toString();
    }

}
