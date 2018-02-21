
package com.blind.wakemeup.weather.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "3h"
})
public class Snow implements Serializable
{

    @JsonProperty("3h")
    public Integer _3h;
    private final static long serialVersionUID = -617497890936060479L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("_3h", _3h).toString();
    }

}
