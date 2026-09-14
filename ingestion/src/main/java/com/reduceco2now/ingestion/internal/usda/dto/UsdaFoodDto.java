package com.reduceco2now.ingestion.internal.usda.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodDto(
        @JsonProperty("gtinUpc") String barcode,
        @JsonProperty("description") String name,
        @JsonProperty("brandOwner") String brand,
        @JsonProperty("foodCategory") String categoryCode
) {}