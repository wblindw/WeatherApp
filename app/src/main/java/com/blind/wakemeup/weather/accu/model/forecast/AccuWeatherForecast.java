
package com.blind.wakemeup.weather.accu.model.forecast;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Headline",
    "DailyForecasts"
})
public class AccuWeatherForecast {

    @JsonProperty("Headline")
    public Headline headline;
    @JsonProperty("DailyForecasts")
    public List<DailyForecast> dailyForecasts = new ArrayList<DailyForecast>();

}
