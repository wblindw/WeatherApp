
package com.blind.wakemeup.weather.model.dailyforecast;

import com.blind.wakemeup.weather.model.common.Clouds;
import com.blind.wakemeup.weather.model.common.Rain;
import com.blind.wakemeup.weather.model.common.Snow;
import com.blind.wakemeup.weather.model.common.Weather;
import com.blind.wakemeup.weather.model.common.Wind;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dt",
    "main",
    "weather",
    "clouds",
    "wind",
    "rain",
    "snow",
    "sys",
    "dt_txt"
})
public class List implements Serializable
{

    @JsonProperty("dt")
    public Integer dt;
    @JsonProperty("main")
    public Main main;
    @JsonProperty("weather")
    public java.util.List<Weather> weather = new ArrayList<Weather>();
    @JsonProperty("clouds")
    public Clouds clouds;
    @JsonProperty("wind")
    public Wind wind;
    @JsonProperty("rain")
    public Rain rain;
    @JsonProperty("snow")
    public Snow snow;
    @JsonProperty("sys")
    public Sys sys;
    @JsonProperty("dt_txt")
    public String dtTxt;
    private final static long serialVersionUID = 5584584291537758700L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dt", dt).append("main", main).append("weather", weather).append("clouds", clouds).append("wind", wind).append("rain", rain).append("snow", snow).append("sys", sys).append("dtTxt", dtTxt).toString();
    }

}
