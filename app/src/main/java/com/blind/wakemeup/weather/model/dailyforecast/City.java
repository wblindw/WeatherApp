
package com.blind.wakemeup.weather.model.dailyforecast;

import com.blind.wakemeup.weather.model.common.Coord;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "coord",
    "country",
    "population"
})
public class City implements Serializable
{

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("coord")
    public Coord coord;
    @JsonProperty("country")
    public String country;
    @JsonProperty("population")
    public Integer population;


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("country", country).append("ccord", coord).append("population", population).toString();
    }

}
