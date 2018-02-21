
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Icon",
    "IconPhrase",
    "ShortPhrase",
    "LongPhrase",
    "PrecipitationProbability",
    "ThunderstormProbability",
    "RainProbability",
    "SnowProbability",
    "IceProbability",
    "Wind",
    "WindGust",
    "TotalLiquid",
    "Rain",
    "Snow",
    "Ice",
    "HoursOfPrecipitation",
    "HoursOfRain",
    "HoursOfSnow",
    "HoursOfIce",
    "CloudCover"
})
public class Day {

    @JsonProperty("Icon")
    public Integer icon;
    @JsonProperty("IconPhrase")
    public String iconPhrase;
    @JsonProperty("ShortPhrase")
    public String shortPhrase;
    @JsonProperty("LongPhrase")
    public String longPhrase;
    @JsonProperty("PrecipitationProbability")
    public Integer precipitationProbability;
    @JsonProperty("ThunderstormProbability")
    public Integer thunderstormProbability;
    @JsonProperty("RainProbability")
    public Integer rainProbability;
    @JsonProperty("SnowProbability")
    public Integer snowProbability;
    @JsonProperty("IceProbability")
    public Integer iceProbability;
    @JsonProperty("Wind")
    public Wind wind;
    @JsonProperty("WindGust")
    public WindGust windGust;
    @JsonProperty("TotalLiquid")
    public TotalLiquid totalLiquid;
    @JsonProperty("Rain")
    public Rain rain;
    @JsonProperty("Snow")
    public Snow snow;
    @JsonProperty("Ice")
    public Ice ice;
    @JsonProperty("HoursOfPrecipitation")
    public Integer hoursOfPrecipitation;
    @JsonProperty("HoursOfRain")
    public Integer hoursOfRain;
    @JsonProperty("HoursOfSnow")
    public Integer hoursOfSnow;
    @JsonProperty("HoursOfIce")
    public Integer hoursOfIce;
    @JsonProperty("CloudCover")
    public Integer cloudCover;

}
