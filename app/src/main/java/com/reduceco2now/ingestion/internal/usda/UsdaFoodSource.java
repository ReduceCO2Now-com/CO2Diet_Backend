package com.reduceco2now.ingestion.internal.usda;

import java.util.Optional;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.FoodSource;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Fetches foods from USDA and converts them into FoodUpsert objects.
 */
public class UsdaFoodSource implements FoodSource {

    private final UsdaClient client;
    private final UsdaFoodMapper mapper;

    public UsdaFoodSource(UsdaClient client, UsdaFoodMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public List<FoodUpsert> fetchFoods() {

        List<FoodUpsert> foodUpserts = new ArrayList<>();

        for (UsdaFoodDto dto : client.fetchFoods()) {

            Optional<FoodUpsert> foodUpsert = mapper.map(dto);
            foodUpsert.ifPresent(foodUpserts::add);
        }

        return foodUpserts;
    }
}