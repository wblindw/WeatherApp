
package com.blind.wakemeup.weather.accu.model.forecast;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Date",
    "EpochDate",
    "Sun",
    "Moon",
    "Temperature",
    "RealFeelTemperature",
    "RealFeelTemperatureShade",
    "HoursOfSun",
    "DegreeDaySummary",
    "AirAndPollen",
    "Day",
    "Night",
    "Sources",
    "MobileLink",
    "Link"
})
public class DailyForecast {

    @JsonProperty("Date")
    public String date;
    @JsonProperty("EpochDate")
    public Integer epochDate;
    @JsonProperty("Sun")
    public Sun sun;
    @JsonProperty("Moon")
    public Moon moon;
    @JsonProperty("Temperature")
    public Temperature temperature;
    @JsonProperty("RealFeelTemperature")
    public RealFeelTemperature realFeelTemperature;
    @JsonProperty("RealFeelTemperatureShade")
    public RealFeelTemperatureShade realFeelTemperatureShade;
    @JsonProperty("HoursOfSun")
    public Double hoursOfSun;
    @JsonProperty("DegreeDaySummary")
    public DegreeDaySummary degreeDaySummary;
    @JsonProperty("AirAndPollen")
    public List<AirAndPollen> airAndPollen = new ArrayList<AirAndPollen>();
    @JsonProperty("Day")
    public Day day;
    @JsonProperty("Night")
    public Night night;
    @JsonProperty("Sources")
    public List<String> sources = new ArrayList<String>();
    @JsonProperty("MobileLink")
    public String mobileLink;
    @JsonProperty("Link")
    public String link;

}
