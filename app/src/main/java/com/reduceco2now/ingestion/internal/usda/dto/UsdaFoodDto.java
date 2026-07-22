package com.reduceco2now.ingestion.internal.usda.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents one food returned by the USDA FoodData Central API.
 * only the fields necessary for the application are kept.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodDto(

        @JsonProperty("gtinUpc")
        String barcode,

        @JsonProperty("description")
        String name,

        @JsonProperty("brandOwner")
        String brand,

        @JsonProperty("dataType")
        String categoryCode

) {
}