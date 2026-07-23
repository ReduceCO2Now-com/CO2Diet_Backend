package com.reduceco2now.ingestion.internal.usda;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
                "Branded"
        );

        // Act
        FoodUpsert result = mapper.map(dto);

        // Assert
        assertNotNull(result);
        assertEquals("123456789", result.barcode());
        assertEquals("Apple", result.name());
        assertEquals("Fresh Farms", result.brand());
        assertEquals("Branded", result.categoryCode());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {

        // Verify that a null DTO is treated as an invalid input.

        // Arrange
        UsdaFoodDto dto = null;

        // Act
        FoodUpsert result = mapper.map(dto);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenNameIsNull() {

        // Verify that a DTO without a name is treated as invalid.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                "123456789",
                null,
                "Fresh Farms",
                "Branded"
        );

        // Act
        FoodUpsert result = mapper.map(dto);

        // Assert
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenNameIsBlank() {

        // Verify that a DTO with a blank name is treated as invalid.

        // Arrange
        UsdaFoodDto dto = new UsdaFoodDto(
                "123456789",
                "   ",
                "Fresh Farms",
                "Branded"
        );

        // Act
        FoodUpsert result = mapper.map(dto);

        // Assert
        assertNull(result);
    }
}