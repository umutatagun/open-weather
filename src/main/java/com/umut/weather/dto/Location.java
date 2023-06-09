package com.umut.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Location(
        String name,
        String country,
        @JsonProperty("localtime")
        String localTime,
        String region
) {
}
