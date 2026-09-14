package com.reduceco2now.ingestion.internal.off;

import com.fasterxml.jackson.databind.JsonNode;
import com.reduceco2now.catalog.FoodUpsert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OffProductMapper {

    private static final Logger log = LoggerFactory.getLogger(OffProductMapper.class);
    public static final String SOURCE_NAME = "off";

    private static final Map<String, String> CATEGORY_MAP = Map.ofEntries(
            Map.entry("en:beverages", "BEVERAGES"),
            Map.entry("en:sodas", "BEVERAGES"),
            Map.entry("en:waters", "BEVERAGES"),
            Map.entry("en:dairies", "DAIRY"),
            Map.entry("en:cheeses", "DAIRY"),
            Map.entry("en:milks", "DAIRY"),
            Map.entry("en:meats", "MEAT"),
            Map.entry("en:seafood", "SEAFOOD"),
            Map.entry("en:fishes", "SEAFOOD"),
            Map.entry("en:snacks", "SNACKS"),
            Map.entry("en:chocolates", "SNACKS"),
            Map.entry("en:biscuits-and-cakes", "SNACKS"),
            Map.entry("en:cereals-and-potatoes", "GRAINS"),
            Map.entry("en:breads", "GRAINS"),
            Map.entry("en:breakfast-cereals", "GRAINS"),
            Map.entry("en:fruits-and-vegetables", "PRODUCE"),
            Map.entry("en:fruits", "PRODUCE"),
            Map.entry("en:vegetables", "PRODUCE"),
            Map.entry("en:fats", "FATS_OILS"),
            Map.entry("en:condiments", "CONDIMENTS"),
            Map.entry("en:sauces", "CONDIMENTS"),
            Map.entry("en:frozen-foods", "FROZEN")
    );

    public Optional<FoodUpsert> map(JsonNode product) {
        if (product == null || product.isMissingNode() || product.isNull()) {
            log.warn("Skipping OFF product: node was null/missing");
            return Optional.empty();
        }

        String code = textOrNull(product, "code");
        if (code == null || code.isBlank()) {
            log.warn("Skipping OFF product: missing required 'code' field. Raw node: {}", product);
            return Optional.empty();
        }

        String name = textOrNull(product, "product_name");
        if (name == null || name.isBlank()) {
            log.warn("Skipping OFF product code={}: missing required 'product_name' field", code);
            return Optional.empty();
        }

        String brand = firstOf(textOrNull(product, "brands"));
        List<String> rawCategories = readStringArray(product.get("categories_tags"));
        String categoryCode = resolveCategoryCode(code, rawCategories);

        return Optional.of(new FoodUpsert(code, name.trim(), brand, categoryCode));
    }

    public List<FoodUpsert> mapBatch(List<JsonNode> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        List<FoodUpsert> result = new ArrayList<>(products.size());
        for (JsonNode product : products) {
            try {
                map(product).ifPresent(result::add);
            } catch (RuntimeException e) {
                log.warn("Skipping malformed OFF product node", e);
            }
        }
        return result;
    }

    private String resolveCategoryCode(String productCode, List<String> rawCategories) {
        if (rawCategories.isEmpty()) {
            log.warn("OFF product code={} has no categories_tags; leaving categoryCode unmapped", productCode);
            return null;
        }
        for (String tag : rawCategories) {
            String mapped = CATEGORY_MAP.get(tag);
            if (mapped != null) {
                return mapped;
            }
        }
        log.warn("OFF product code={} has no known category mapping for tags {}; flagging for manual review",
                productCode, rawCategories);
        return null;
    }

    private static String firstOf(String commaSeparated) {
        if (commaSeparated == null || commaSeparated.isBlank()) {
            return null;
        }
        String[] parts = commaSeparated.split(",");
        return parts[0].trim().isEmpty() ? null : parts[0].trim();
    }

    private static String textOrNull(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull() || !value.isTextual()) {
            return null;
        }
        String text = value.asText();
        return text.isBlank() ? null : text;
    }

    private static List<String> readStringArray(JsonNode arrayNode) {
        List<String> values = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            arrayNode.forEach(n -> {
                if (n.isTextual() && !n.asText().isBlank()) {
                    values.add(n.asText());
                }
            });
        }
        return values;
    }
}