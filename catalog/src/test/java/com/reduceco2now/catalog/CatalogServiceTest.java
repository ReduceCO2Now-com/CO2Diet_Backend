package com.reduceco2now.catalog;

import com.reduceco2now.catalog.Food;
import com.reduceco2now.catalog.FoodProductEntity;
import com.reduceco2now.catalog.FoodProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
class CatalogServiceTest {

    @Test
    void shouldReturnFoodWhenIdExists() {

        // Arrange
        FoodProductRepository repo = Mockito.mock(FoodProductRepository.class);

        CatalogService service = new CatalogService(repo);

        FoodProductEntity entity = new FoodProductEntity(
                1L,
                "123456789",
                "Apple",
                "Fresh Farm",
                "FRUIT",
                1L
        );

        when(repo.findById(1L))
        .thenReturn(Optional.of(entity));

        // Act
        Optional<Food> result = service.byId(1L);


        // Asssert

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("123456789", result.get().barcode());
        assertEquals("Apple", result.get().name());
        assertEquals("Fresh Farm", result.get().brand());
        assertEquals("FRUIT", result.get().categoryCode());
        assertEquals(1L, result.get().rowVersion());



    }
    @Test
    void shouldReturnEmptyWhenIdDoeseNotExist() {

        //Arrange
        FoodProductRepository repo = Mockito.mock(FoodProductRepository.class);

        CatalogService service = new CatalogService(repo);

        when(repo.findById(1L))
        .thenReturn(Optional.empty());

        // Act
        Optional<Food> result = service.byId(1L);


        // Assert
        assertTrue(result.isEmpty());



    }
    @Test
    void shouldReturnFoodWhenBarcodeExists() {

        //Arrange
        FoodProductRepository repo = Mockito.mock(FoodProductRepository.class);

        CatalogService service = new CatalogService(repo);

        FoodProductEntity entity = new FoodProductEntity(
                1L,
                "123456789",
                "Apple",
                "Fresh Farm",
                "FRUIT",
                1L
        );

        when(repo.findByBarcode("123456789"))
        .thenReturn(Optional.of(entity));

        //Act
        Optional<Food> result = service.byBarcode("123456789");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals("123456789", result.get().barcode());
        assertEquals("Apple", result.get().name());
        assertEquals("Fresh Farm", result.get().brand());
        assertEquals("FRUIT", result.get().categoryCode());
        assertEquals(1L, result.get().rowVersion());


    }
    @Test
    void shouldReturnFoodWhenBarcodeDoesNotExist(){

        //Arrange
        FoodProductRepository repo = Mockito.mock(FoodProductRepository.class);

        CatalogService service = new CatalogService(repo);


        when(repo.findByBarcode("123456789"))
        .thenReturn(Optional.empty());

        //Act
        Optional<Food> result = service.byBarcode("123456789");

        //Assert
         assertTrue(result.isEmpty());






    }
    @Test
    void shouldReturnFoodMatchingSearch() {
        //Arrange
        FoodProductRepository repo = Mockito.mock(FoodProductRepository.class);

        CatalogService service = new CatalogService(repo);

        FoodProductEntity apple = new FoodProductEntity(
        1L,
        "111",
        "Apple",
        "Fresh Farm",
        "FRUIT",
        1L

        );

        FoodProductEntity banana = new FoodProductEntity(
        2L,
        "222",
        "Banana",
        "Fresh Farm",
        "FRUIT",
        1L
        );
        when(repo.search("a", "FRUIT"))
             .thenReturn(List.of(apple, banana));

        //Act
        List<Food> result = service.search("a", "FRUIT", 10);

        //Assert
        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).name());
        assertEquals("Banana", result.get(1).name());

        //Verify interaction
        verify(repo).search("a", "FRUIT");




    }
    @Test
     void shouldRespectLimit() {

        //Arrange
        FoodProductRepository repo = Mockito.mock(FoodProductRepository.class);

        CatalogService service = new CatalogService(repo);

        FoodProductEntity apple = new FoodProductEntity(
        1L,
        "111",
        "Apple",
        "Fresh Farm",
        "FRUIT",
        1L

        );

        FoodProductEntity banana = new FoodProductEntity(
        2L,
        "222",
        "Banana",
        "Fresh Farm",
        "FRUIT",
        1L

        );



        when(repo.search("a", "FRUIT"))
             .thenReturn(List.of(apple, banana));

        //Act
        List<Food> result = service.search("a", "FRUIT", 1);

        //Assert
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).name());

    }




}
