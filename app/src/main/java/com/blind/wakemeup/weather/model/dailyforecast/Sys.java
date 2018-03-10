
package com.blind.wakemeup.weather.model.dailyforecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pod"
})
public class Sys implements Serializable
{

    @JsonProperty("pod")
    public String pod;
    private final static long serialVersionUID = -7497973180796332658L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("pod", pod).toString();
    }

}
