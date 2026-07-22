package com.reduceco2now.ingestion.internal.usda;

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

            FoodUpsert foodUpsert = mapper.map(dto);

            // Skip malformed entries instead of stopping the batch.
            if (foodUpsert != null) {
                foodUpserts.add(foodUpsert);
            }
        }

        return foodUpserts;
    }
}