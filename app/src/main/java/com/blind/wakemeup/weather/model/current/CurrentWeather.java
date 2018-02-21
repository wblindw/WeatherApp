
package com.blind.wakemeup.weather.model.current;

import com.blind.wakemeup.weather.model.common.Clouds;
import com.blind.wakemeup.weather.model.common.Coord;
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
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "cod",
    "coord",
    "weather",
    "base",
    "main",
    "visibility",
    "wind",
    "clouds",
    "rain",
    "snow",
    "dt",
    "sys"
})
public class CurrentWeather implements Serializable
{
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("cod")
    public Integer cod;
    @JsonProperty("coord")
    public Coord coord;
    @JsonProperty("weather")
    public List<Weather> weather = new ArrayList<Weather>();
    @JsonProperty("base")
    public String base;
    @JsonProperty("main")
    public Main main;
    @JsonProperty("visibility")
    public Integer visibility;
    @JsonProperty("wind")
    public Wind wind;
    @JsonProperty("clouds")
    public Clouds clouds;
    @JsonProperty("rain")
    public Rain rain;
    @JsonProperty("snow")
    public Snow snow;
    @JsonProperty("dt")
    public Integer dt;
    @JsonProperty("sys")
    public Sys sys;
    private final static long serialVersionUID = -6878051131725022227L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("cod", cod).append("coord", coord).append("weather", weather).append("base", base).append("main", main).append("visibility", visibility).append("wind", wind).append("clouds", clouds).append("rain", rain).append("snow", snow).append("dt", dt).append("sys", sys).toString();
    }

}
