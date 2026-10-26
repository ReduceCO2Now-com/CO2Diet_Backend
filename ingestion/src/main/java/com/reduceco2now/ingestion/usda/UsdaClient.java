package com.reduceco2now.ingestion.usda;

import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;


import java.util.List;

/**
 * Client responsible for communicating with the USDA FoodData Central API.
 */
public class UsdaClient {

    private static final String BASE_URL = "https://api.nal.usda.gov/fdc/v1";
    private static final int PAGE_SIZE = 100;
    private static final int PAGE_NUMBER = 1;

    private final RestClient restClient;
    private final String apiKey;

    public UsdaClient(String apiKey) {
        this.restClient = RestClient.create(BASE_URL);
        this.apiKey = apiKey;
    }

    /**
     * Fetches one batch of foods from the USDA API.
     *
     * <p>Filtered to dataType=Branded, since that is the only data type
     * carrying gtinUpc (barcode) and foodCategory, the fields our mapper needs.
     */
    public List<UsdaFoodDto> fetchFoods() {
        List<UsdaFoodDto> foods = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/foods/list")
                        .queryParam("api_key", apiKey)
                        .queryParam("dataType", "Branded")
                        .queryParam("pageSize", PAGE_SIZE)
                        .queryParam("pageNumber", PAGE_NUMBER)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<UsdaFoodDto>>() {});
        return foods == null ? List.of() : foods;
    }
}