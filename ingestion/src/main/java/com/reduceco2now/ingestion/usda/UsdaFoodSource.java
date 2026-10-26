package com.reduceco2now.ingestion.usda;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.FoodSource;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UsdaFoodSource implements FoodSource {

    private final UsdaClient client;
    private final UsdaFoodMapper mapper;

    public UsdaFoodSource(UsdaClient client, UsdaFoodMapper mapper) {
        this.client = Objects.requireNonNull(client, "client");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    @Override
    public List<FoodUpsert> fetchFoods() {
        List<FoodUpsert> result = new ArrayList<>();
        for (UsdaFoodDto dto : client.fetchFoods()) {
            mapper.map(dto).ifPresent(result::add);
        }
        return result;
    }
}
