
package com.blind.wakemeup.weather.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "all"
})
public class Clouds implements Serializable
{

    @JsonProperty("all")
    public Integer all;
    private final static long serialVersionUID = -234975317465451622L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("all", all).toString();
    }

}
