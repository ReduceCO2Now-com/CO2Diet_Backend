package com.reduceco2now.ingestion.internal.usda;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsdaFoodMapper {

    private static final Logger log = LoggerFactory.getLogger(UsdaFoodMapper.class);

    private static final Map<String, String> CATEGORY_MAP = Map.ofEntries(
            Map.entry("Cheese", "DAIRY"),
            Map.entry("Milk", "DAIRY"),
            Map.entry("Yogurt", "DAIRY"),
            Map.entry("Poultry, Chicken & Turkey", "MEAT"),
            Map.entry("Poultry Products", "MEAT"),
            Map.entry("Beef", "MEAT"),
            Map.entry("Pork", "MEAT"),
            Map.entry("Sausages", "MEAT"),
            Map.entry("Fish & Seafood", "SEAFOOD"),
            Map.entry("Popcorn, Peanuts, Seeds & Related Snacks", "SNACKS"),
            Map.entry("Chips, Pretzels & Snacks", "SNACKS"),
            Map.entry("Candy", "SNACKS"),
            Map.entry("Cookies & Biscuits", "SNACKS"),
            Map.entry("Chocolate", "SNACKS"),
            Map.entry("Bread & Baked Goods", "GRAINS"),
            Map.entry("Breakfast Cereals", "GRAINS"),
            Map.entry("Pasta", "GRAINS"),
            Map.entry("Rice", "GRAINS"),
            Map.entry("Vegetables", "PRODUCE"),
            Map.entry("Fruit & Fruit Juices", "PRODUCE"),
            Map.entry("Fats & Oils", "FATS_OILS"),
            Map.entry("Condiments & Sauces", "CONDIMENTS"),
            Map.entry("Soft Drinks", "BEVERAGES"),
            Map.entry("Water", "BEVERAGES"),
            Map.entry("Frozen Vegetables", "FROZEN"),
            Map.entry("Ice Cream & Frozen Yogurt", "FROZEN")
    );

    public Optional<FoodUpsert> map(UsdaFoodDto dto) {
        if (dto == null
                || dto.barcode() == null || dto.barcode().isBlank()
                || dto.name() == null || dto.name().isBlank()) {
            return Optional.empty();
        }
        String categoryCode = resolveCategoryCode(dto.barcode(), dto.categoryCode());
        return Optional.of(new FoodUpsert(dto.barcode(), dto.name(), dto.brand(), categoryCode));
    }

    private String resolveCategoryCode(String barcode, String rawCategory) {
        if (rawCategory == null || rawCategory.isBlank()) {
            log.warn("USDA product barcode={} has no foodCategory; leaving categoryCode unmapped", barcode);
            return null;
        }
        String mapped = CATEGORY_MAP.get(rawCategory);
        if (mapped != null) {
            return mapped;
        }
        log.warn("USDA product barcode={} has no known category mapping for '{}'; flagging for manual review",
                barcode, rawCategory);
        return null;
    }
}