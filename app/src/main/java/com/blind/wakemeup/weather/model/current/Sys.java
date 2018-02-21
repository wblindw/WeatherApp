
package com.blind.wakemeup.weather.model.current;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "id",
    "message",
    "country",
    "sunrise",
    "sunset"
})
public class Sys implements Serializable
{

    @JsonProperty("type")
    public Integer type;
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("message")
    public Float message;
    @JsonProperty("country")
    public String country;
    @JsonProperty("sunrise")
    public Integer sunrise;
    @JsonProperty("sunset")
    public Integer sunset;
    private final static long serialVersionUID = 109533728711464697L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("id", id).append("message", message).append("country", country).append("sunrise", sunrise).append("sunset", sunset).toString();
    }

}
