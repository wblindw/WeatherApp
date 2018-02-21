
package com.blind.wakemeup.weather.accu.model.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Name",
    "Value",
    "Category",
    "CategoryValue",
    "Type"
})
public class AirAndPollen {

    @JsonProperty("Name")
    public String name;
    @JsonProperty("Value")
    public Integer value;
    @JsonProperty("Category")
    public String category;
    @JsonProperty("CategoryValue")
    public Integer categoryValue;
    @JsonProperty("Type")
    public String type;

}
