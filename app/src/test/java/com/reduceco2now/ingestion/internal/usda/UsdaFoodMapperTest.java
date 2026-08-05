package com.reduceco2now.ingestion.internal.usda;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsdaFoodMapperTest {

    private final UsdaFoodMapper mapper = new UsdaFoodMapper();

    @Test
    void shouldMapValidDto() {

        // Verify that a valid USDA food is mapped correctly to a FoodUpsert object.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                "123456789",
                "Apple",
                "Fresh Farms",
                null);

        // Act
        Optional<FoodUpsert> result = mapper.map(dto);

        // Assert
        assertTrue(result.isPresent());

        FoodUpsert food = result.get();

        assertEquals("123456789", food.barcode());
        assertEquals("Apple", food.name());
        assertEquals("Fresh Farms", food.brand());
        assertNull(food.categoryCode());
    }

    @Test
    void shouldReturnEmptyWhenDtoIsNull() {

        // Verify that a null DTO is treated as invalid input.

        // Arrange
        UsdaFoodDto dto = null;

        // Act
        Optional<FoodUpsert> result = mapper.map(dto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenBarcodeIsNull() {

        // Verify that a DTO without a barcode is treated as invalid.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                null,
                "Apple",
                "Fresh Farms",
                null);

        // Act
        Optional<FoodUpsert> result = mapper.map(dto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenBarcodeIsBlank() {

        // Verify that a DTO with a blank barcode is treated as invalid.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                "   ",
                "Apple",
                "Fresh Farms",
                null);

        // Act
        Optional<FoodUpsert> result = mapper.map(dto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenNameIsNull() {

        // Verify that a DTO without a name is treated as invalid.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                "123456789",
                null,
                "Fresh Farms",
                null);

        // Act
        Optional<FoodUpsert> result = mapper.map(dto);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenNameIsBlank() {

        // Verify that a DTO with a blank name is treated as invalid.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                "123456789",
                "   ",
                "Fresh Farms",
                null);

        // Act
        Optional<FoodUpsert> result = mapper.map(dto);

        // Assert
        assertTrue(result.isEmpty());
    }
}