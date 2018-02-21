
package com.blind.wakemeup.weather.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "main",
    "description",
    "icon"
})
public class Weather implements Serializable
{

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("main")
    public String main;
    @JsonProperty("description")
    public String description;
    @JsonProperty("icon")
    public String icon;
    private final static long serialVersionUID = 8761084290691976280L;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("main", main).append("description", description).append("icon", icon).toString();
    }

}
