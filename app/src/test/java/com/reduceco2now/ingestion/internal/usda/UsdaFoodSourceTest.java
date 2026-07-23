package com.reduceco2now.ingestion.internal.usda;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.internal.usda.dto.UsdaFoodDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class UsdaFoodSourceTest {

        // Create fake dependencies for the class under test.
        private final UsdaClient client = mock(UsdaClient.class);
        private final UsdaFoodMapper mapper = mock(UsdaFoodMapper.class);

        // Create the object we want to test.
        private final UsdaFoodSource foodSource = new UsdaFoodSource(client, mapper);

        @Test
        void shouldReturnMappedFoods() {

                // Verify that all valid USDA foods are mapped and returned.

                // Arrange
                UsdaFoodDto appleDto = new UsdaFoodDto(
                                "111",
                                "Apple",
                                "Fresh Farms",
                                "Branded");

                UsdaFoodDto orangeDto = new UsdaFoodDto(
                                "222",
                                "Orange",
                                "Fresh Farms",
                                "Branded");

                FoodUpsert apple = new FoodUpsert(
                                "111",
                                "Apple",
                                "Fresh Farms",
                                "Branded");

                FoodUpsert orange = new FoodUpsert(
                                "222",
                                "Orange",
                                "Fresh Farms",
                                "Branded");

                when(client.fetchFoods()).thenReturn(List.of(appleDto, orangeDto));

                when(mapper.map(appleDto)).thenReturn(apple);
                when(mapper.map(orangeDto)).thenReturn(orange);

                // Act
                List<FoodUpsert> result = foodSource.fetchFoods();

                // Assert
                assertEquals(2, result.size());
                assertEquals(apple, result.get(0));
                assertEquals(orange, result.get(1));
        }

        @Test
        void shouldSkipMalformedFoods() {

                // Verify that malformed foods are skipped and valid foods are returned.

                // Arrange
                UsdaFoodDto appleDto = new UsdaFoodDto(
                                "111",
                                "Apple",
                                "Fresh Farms",
                                "Branded");

                UsdaFoodDto invalidDto = new UsdaFoodDto(
                                "222",
                                null,
                                "Fresh Farms",
                                "Branded");

                FoodUpsert apple = new FoodUpsert(
                                "111",
                                "Apple",
                                "Fresh Farms",
                                "Branded");

                when(client.fetchFoods()).thenReturn(List.of(appleDto, invalidDto));

                when(mapper.map(appleDto)).thenReturn(apple);
                when(mapper.map(invalidDto)).thenReturn(null);

                // Act
                List<FoodUpsert> result = foodSource.fetchFoods();

                // Assert
                assertEquals(1, result.size());
                assertEquals(apple, result.getFirst());
        }

        @Test
        void shouldCallMapperForEachFood() {

                // Verify that every USDA food returned by the client is passed to the mapper.

                // Arrange
                UsdaFoodDto appleDto = new UsdaFoodDto(
                                "111",
                                "Apple",
                                "Fresh Farms",
                                "Branded");

                UsdaFoodDto orangeDto = new UsdaFoodDto(
                                "222",
                                "Orange",
                                "Fresh Farms",
                                "Branded");

                FoodUpsert apple = new FoodUpsert(
                                "111",
                                "Apple",
                                "Fresh Farms",
                                "Branded");

                FoodUpsert orange = new FoodUpsert(
                                "222",
                                "Orange",
                                "Fresh Farms",
                                "Branded");

                when(client.fetchFoods()).thenReturn(List.of(appleDto, orangeDto));

                when(mapper.map(appleDto)).thenReturn(apple);
                when(mapper.map(orangeDto)).thenReturn(orange);

                // Act
                foodSource.fetchFoods();

                // Assert
                verify(client).fetchFoods();
                verify(mapper).map(appleDto);
                verify(mapper).map(orangeDto);
        }
}