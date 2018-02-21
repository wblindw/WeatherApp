
package com.blind.wakemeup.weather.accu.model.day;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "LocalObservationDateTime",
    "EpochTime",
    "WeatherText",
    "WeatherIcon",
    "IsDayTime",
    "Temperature",
    "RealFeelTemperature",
    "RealFeelTemperatureShade",
    "RelativeHumidity",
    "DewPoint",
    "Wind",
    "WindGust",
    "UVIndex",
    "UVIndexText",
    "Visibility",
    "ObstructionsToVisibility",
    "CloudCover",
    "Ceiling",
    "Pressure",
    "PressureTendency",
    "Past24HourTemperatureDeparture",
    "ApparentTemperature",
    "WindChillTemperature",
    "WetBulbTemperature",
    "Precip1hr",
    "PrecipitationSummary",
    "TemperatureSummary",
    "MobileLink",
    "Link"
})
public class AccuWeatherCurrent {

    @JsonProperty("LocalObservationDateTime")
    public String localObservationDateTime;
    @JsonProperty("EpochTime")
    public Integer epochTime;
    @JsonProperty("WeatherText")
    public String weatherText;
    @JsonProperty("WeatherIcon")
    public Integer weatherIcon;
    @JsonProperty("IsDayTime")
    public Boolean isDayTime;
    @JsonProperty("Temperature")
    public Temperature temperature;
    @JsonProperty("RealFeelTemperature")
    public RealFeelTemperature realFeelTemperature;
    @JsonProperty("RealFeelTemperatureShade")
    public RealFeelTemperatureShade realFeelTemperatureShade;
    @JsonProperty("RelativeHumidity")
    public Integer relativeHumidity;
    @JsonProperty("DewPoint")
    public DewPoint dewPoint;
    @JsonProperty("Wind")
    public Wind wind;
    @JsonProperty("WindGust")
    public WindGust windGust;
    @JsonProperty("UVIndex")
    public Integer uVIndex;
    @JsonProperty("UVIndexText")
    public String uVIndexText;
    @JsonProperty("Visibility")
    public Visibility visibility;
    @JsonProperty("ObstructionsToVisibility")
    public String obstructionsToVisibility;
    @JsonProperty("CloudCover")
    public Integer cloudCover;
    @JsonProperty("Ceiling")
    public Ceiling ceiling;
    @JsonProperty("Pressure")
    public Pressure pressure;
    @JsonProperty("PressureTendency")
    public PressureTendency pressureTendency;
    @JsonProperty("Past24HourTemperatureDeparture")
    public Past24HourTemperatureDeparture past24HourTemperatureDeparture;
    @JsonProperty("ApparentTemperature")
    public ApparentTemperature apparentTemperature;
    @JsonProperty("WindChillTemperature")
    public WindChillTemperature windChillTemperature;
    @JsonProperty("WetBulbTemperature")
    public WetBulbTemperature wetBulbTemperature;
    @JsonProperty("Precip1hr")
    public Precip1hr precip1hr;
    @JsonProperty("PrecipitationSummary")
    public PrecipitationSummary precipitationSummary;
    @JsonProperty("TemperatureSummary")
    public TemperatureSummary temperatureSummary;
    @JsonProperty("MobileLink")
    public String mobileLink;
    @JsonProperty("Link")
    public String link;

}
