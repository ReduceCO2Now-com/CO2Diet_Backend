package com.reduceco2now.ingestion.internal.off;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reduceco2now.ingestion.FoodUpsert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OffProductMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private OffProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OffProductMapper();
    }

    @Test
    void mapsAWellFormedProductWithKnownCategory() {
        ObjectNode product = objectMapper.createObjectNode();
        product.put("code", "3017620422003");
        product.put("product_name", "Nutella");
        product.put("brands", "Ferrero,Nutella");
        product.put("quantity", "400g");
        product.put("image_url", "https://images.example/nutella.jpg");
        ArrayNode categories = product.putArray("categories_tags");
        categories.add("en:spreads");
        categories.add("en:sweet-spreads");
        categories.add("en:chocolates");

        Optional<FoodUpsert> result = mapper.map(product);

        assertThat(result).isPresent();
        FoodUpsert upsert = result.get();
        assertThat(upsert.externalId()).isEqualTo("3017620422003");
        assertThat(upsert.sourceName()).isEqualTo("off");
        assertThat(upsert.name()).isEqualTo("Nutella");
        assertThat(upsert.brand()).isEqualTo("Ferrero");
        assertThat(upsert.quantityText()).isEqualTo("400g");
        assertThat(upsert.imageUrl()).isEqualTo("https://images.example/nutella.jpg");
        // "en:chocolates" is the known tag among the three
        assertThat(upsert.categoryCode()).isEqualTo("SNACKS");
        assertThat(upsert.rawCategories()).containsExactly("en:spreads", "en:sweet-spreads", "en:chocolates");
    }

    @Test
    void flagsGapWhenNoCategoryTagIsKnown() {
        ObjectNode product = objectMapper.createObjectNode();
        product.put("code", "111");
        product.put("product_name", "Mystery Product");
        ArrayNode categories = product.putArray("categories_tags");
        categories.add("en:some-totally-unmapped-category");

        Optional<FoodUpsert> result = mapper.map(product);

        assertThat(result).isPresent();
        assertThat(result.get().categoryCode()).isNull();
        assertThat(result.get().rawCategories()).containsExactly("en:some-totally-unmapped-category");
    }

    @Test
    void leavesCategoryCodeNullWhenNoCategoriesTagsAtAll() {
        ObjectNode product = objectMapper.createObjectNode();
        product.put("code", "222");
        product.put("product_name", "No Category Product");

        Optional<FoodUpsert> result = mapper.map(product);

        assertThat(result).isPresent();
        assertThat(result.get().categoryCode()).isNull();
        assertThat(result.get().rawCategories()).isEmpty();
    }

    @Test
    void skipsProductMissingCode() {
        ObjectNode product = objectMapper.createObjectNode();
        product.put("product_name", "No Code Product");

        assertThat(mapper.map(product)).isEmpty();
    }

    @Test
    void skipsProductMissingName() {
        ObjectNode product = objectMapper.createObjectNode();
        product.put("code", "333");

        assertThat(mapper.map(product)).isEmpty();
    }

    @Test
    void skipsNullNode() {
        assertThat(mapper.map(null)).isEmpty();
        assertThat(mapper.map(objectMapper.nullNode())).isEmpty();
    }

    @Test
    void mapBatchSkipsMalformedEntriesButKeepsGoodOnes() {
        ObjectNode good = objectMapper.createObjectNode();
        good.put("code", "1");
        good.put("product_name", "Good Product");

        ObjectNode missingCode = objectMapper.createObjectNode();
        missingCode.put("product_name", "Bad Product - no code");

        ObjectNode missingName = objectMapper.createObjectNode();
        missingName.put("code", "2");

        List<JsonNode> batch = List.of(good, missingCode, missingName);

        List<FoodUpsert> result = mapper.mapBatch(batch);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).externalId()).isEqualTo("1");
    }

    @Test
    void mapBatchAcceptsANullOrEmptyBatch() {
        assertThat(mapper.mapBatch(null)).isEmpty();
        assertThat(mapper.mapBatch(List.of())).isEmpty();
    }
}
