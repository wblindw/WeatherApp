
package com.blind.wakemeup.weather.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "temp",
    "temp_min",
    "temp_max",
    "pressure",
    "sea_level",
    "grnd_level",
    "humidity",
    "temp_kf"
})
public class Main implements Serializable
{

    @JsonProperty("temp")
    public Float temp;
    @JsonProperty("temp_min")
    public Float tempMin;
    @JsonProperty("temp_max")
    public Float tempMax;
    @JsonProperty("pressure")
    public Float pressure;
    @JsonProperty("sea_level")
    public Float seaLevel;
    @JsonProperty("grnd_level")
    public Float grndLevel;
    @JsonProperty("humidity")
    public Integer humidity;
    @JsonProperty("temp_kf")
    public Float tempKf;
    private final static long serialVersionUID = -350783478059121527L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("temp", temp).append("tempMin", tempMin).append("tempMax", tempMax).append("pressure", pressure).append("seaLevel", seaLevel).append("grndLevel", grndLevel).append("humidity", humidity).append("tempKf", tempKf).toString();
    }

}
