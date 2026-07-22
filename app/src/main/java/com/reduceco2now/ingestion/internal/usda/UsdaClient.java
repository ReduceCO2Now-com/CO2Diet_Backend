package com.reduceco2now.ingestion.internal.usda;

import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

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
     * TODO:
     * Read the API key from configuration once the project's
     * secret management approach is confirmed.
     */
    public List<UsdaFoodDto> fetchFoods() {

        List<UsdaFoodDto> foods = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/foods/list")
                        .queryParam("api_key", apiKey)
                        .queryParam("pageSize", PAGE_SIZE)
                        .queryParam("pageNumber", PAGE_NUMBER)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<UsdaFoodDto>>() {});

        // Return an empty list if the API response has no body.
        return foods != null ? foods : List.of();
    }
}