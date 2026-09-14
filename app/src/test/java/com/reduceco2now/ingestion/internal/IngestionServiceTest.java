package com.reduceco2now.ingestion.internal;

import com.reduceco2now.catalog.CatalogCommand;
import com.reduceco2now.catalog.Food;
import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.FoodSource;
import com.reduceco2now.ingestion.internal.IngestionService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngestionServiceTest {

    private final FoodSource firstSource = mock(FoodSource.class);
    private final FoodSource secondSource = mock(FoodSource.class);
    private final CatalogCommand catalogCommand = mock(CatalogCommand.class);

    private final IngestionService ingestionService = new IngestionService(
            List.of(firstSource, secondSource),
            catalogCommand);

    @Test
    void shouldUpsertEveryItemFromEverySource() {
        FoodUpsert apple = new FoodUpsert("111", "Apple", "Brand A", "Fruit");
        FoodUpsert orange = new FoodUpsert("222", "Orange", "Brand B", "Fruit");
        FoodUpsert banana = new FoodUpsert("333", "Banana", "Brand C", "Fruit");

        when(firstSource.fetchFoods()).thenReturn(List.of(apple, orange));
        when(secondSource.fetchFoods()).thenReturn(List.of(banana));
        when(catalogCommand.upsert(any(FoodUpsert.class))).thenAnswer(invocation -> {
            FoodUpsert upsert = invocation.getArgument(0);
            return new Food(1L, upsert.barcode(), upsert.name(), upsert.brand(), upsert.categoryCode(), 1L);
        });

        ingestionService.runIngestion();

        verify(firstSource).fetchFoods();
        verify(secondSource).fetchFoods();
        verify(catalogCommand, times(3)).upsert(any(FoodUpsert.class));
    }

    @Test
    void shouldContinueWhenOneSourceFails() {
        FoodUpsert banana = new FoodUpsert("333", "Banana", "Brand C", "Fruit");

        when(firstSource.fetchFoods()).thenThrow(new RuntimeException("source failed"));
        when(secondSource.fetchFoods()).thenReturn(List.of(banana));
        when(catalogCommand.upsert(any(FoodUpsert.class))).thenReturn(
                new Food(1L, "333", "Banana", "Brand C", "Fruit", 1L));

        assertDoesNotThrow(() -> ingestionService.runIngestion());

        verify(firstSource).fetchFoods();
        verify(secondSource).fetchFoods();
        verify(catalogCommand).upsert(banana);
    }
}