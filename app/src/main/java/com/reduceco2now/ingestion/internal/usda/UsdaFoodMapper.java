package com.reduceco2now.ingestion.internal.usda;

import java.util.Optional;
import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;

public class UsdaFoodMapper {

    public Optional<FoodUpsert> map(UsdaFoodDto dto) {

        if (dto == null
                || dto.barcode() == null
                || dto.barcode().isBlank()
                || dto.name() == null
                || dto.name().isBlank()) {
            return Optional.empty();
        }

        return Optional.of(
                new FoodUpsert(
                        dto.barcode(),
                        dto.name(),
                        dto.brand(),
                        dto.categoryCode()));
    }
}