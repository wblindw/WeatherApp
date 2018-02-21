
package com.blind.wakemeup.weather.accu.model.day;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Precipitation",
    "PastHour",
    "Past3Hours",
    "Past6Hours",
    "Past9Hours",
    "Past12Hours",
    "Past18Hours",
    "Past24Hours"
})
public class PrecipitationSummary {

    @JsonProperty("Precipitation")
    public Precipitation precipitation;
    @JsonProperty("PastHour")
    public PastHour pastHour;
    @JsonProperty("Past3Hours")
    public Past3Hours past3Hours;
    @JsonProperty("Past6Hours")
    public Past6Hours past6Hours;
    @JsonProperty("Past9Hours")
    public Past9Hours past9Hours;
    @JsonProperty("Past12Hours")
    public Past12Hours past12Hours;
    @JsonProperty("Past18Hours")
    public Past18Hours past18Hours;
    @JsonProperty("Past24Hours")
    public Past24Hours past24Hours;

}
