
package com.blind.wakemeup.weather.model.current;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "temp",
    "pressure",
    "humidity",
    "temp_min",
    "temp_max",
    "seal_level",
    "grnd_level"
})
public class Main implements Serializable
{

    @JsonProperty("temp")
    public Float temp;
    @JsonProperty("pressure")
    public Integer pressure;
    @JsonProperty("humidity")
    public Integer humidity;
    @JsonProperty("temp_min")
    public Float tempMin;
    @JsonProperty("temp_max")
    public Float tempMax;
    @JsonProperty("seal_level")
    public Integer sealLevel;
    @JsonProperty("grnd_level")
    public Integer grndLevel;
    private final static long serialVersionUID = -2784125228503759651L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("temp", temp).append("pressure", pressure).append("humidity", humidity).append("tempMin", tempMin).append("tempMax", tempMax).append("sealLevel", sealLevel).append("grndLevel", grndLevel).toString();
    }

}
