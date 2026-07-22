package com.reduceco2now.ingestion.internal.usda;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;

public class UsdaFoodMapper {

    public FoodUpsert map(UsdaFoodDto dto) {

        if (dto == null
                || dto.name() == null 
                || dto.name().isBlank()) {
            return null;
        }

        return new FoodUpsert(
                dto.barcode(),
                dto.name(),
                dto.brand(),
                dto.categoryCode()
        );
    }
}