
package com.blind.wakemeup.weather.accu.model.hourlyforecast;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "DateTime",
    "EpochDateTime",
    "WeatherIcon",
    "IconPhrase",
    "IsDaylight",
    "Temperature",
    "RealFeelTemperature",
    "WetBulbTemperature",
    "DewPoint",
    "Wind",
    "WindGust",
    "RelativeHumidity",
    "Visibility",
    "Ceiling",
    "UVIndex",
    "UVIndexText",
    "PrecipitationProbability",
    "RainProbability",
    "SnowProbability",
    "IceProbability",
    "TotalLiquid",
    "Rain",
    "Snow",
    "Ice",
    "CloudCover",
    "MobileLink",
    "Link"
})
public class HourlyForecast {

    @JsonProperty("DateTime")
    public String dateTime;
    @JsonProperty("EpochDateTime")
    public Integer epochDateTime;
    @JsonProperty("WeatherIcon")
    public Integer weatherIcon;
    @JsonProperty("IconPhrase")
    public String iconPhrase;
    @JsonProperty("IsDaylight")
    public Boolean isDaylight;
    @JsonProperty("Temperature")
    public Temperature temperature;
    @JsonProperty("RealFeelTemperature")
    public RealFeelTemperature realFeelTemperature;
    @JsonProperty("WetBulbTemperature")
    public WetBulbTemperature wetBulbTemperature;
    @JsonProperty("DewPoint")
    public DewPoint dewPoint;
    @JsonProperty("Wind")
    public Wind wind;
    @JsonProperty("WindGust")
    public WindGust windGust;
    @JsonProperty("RelativeHumidity")
    public Integer relativeHumidity;
    @JsonProperty("Visibility")
    public Visibility visibility;
    @JsonProperty("Ceiling")
    public Ceiling ceiling;
    @JsonProperty("UVIndex")
    public Integer uVIndex;
    @JsonProperty("UVIndexText")
    public String uVIndexText;
    @JsonProperty("PrecipitationProbability")
    public Integer precipitationProbability;
    @JsonProperty("RainProbability")
    public Integer rainProbability;
    @JsonProperty("SnowProbability")
    public Integer snowProbability;
    @JsonProperty("IceProbability")
    public Integer iceProbability;
    @JsonProperty("TotalLiquid")
    public TotalLiquid totalLiquid;
    @JsonProperty("Rain")
    public Rain rain;
    @JsonProperty("Snow")
    public Snow snow;
    @JsonProperty("Ice")
    public Ice ice;
    @JsonProperty("CloudCover")
    public Integer cloudCover;
    @JsonProperty("MobileLink")
    public String mobileLink;
    @JsonProperty("Link")
    public String link;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
