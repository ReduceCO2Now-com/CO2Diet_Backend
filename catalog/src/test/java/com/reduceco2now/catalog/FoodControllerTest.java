package com.reduceco2now.catalog;

import com.reduceco2now.catalog.CatalogQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.reduceco2now.catalog.Food;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FoodController.class)
class FoodControllerTest {
  @Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private CatalogQuery catalog;

        @Test
        void shouldReturnFoodWhenIdExists() throws Exception {

                // Arrange
                 Food food = new Food(
                  1L,
                  "123456789",
                  "Apple",
                  "Fresh Farm",
                  "FRUIT",
                  1L

             );

             when(catalog.byId(1L))
                .thenReturn(Optional.of(food));

                // Act + Assert
             mockMvc.perform(get("/api/v1/foods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.barcode").value("123456789"))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.brand").value("Fresh Farm"))
                .andExpect(jsonPath("$.categoryCode").value("FRUIT"))
                .andExpect(jsonPath("$.rowVersion").value(1));

      }
      @Test
      void shouldReturnFoodWhenBarcodeExists() throws Exception {

             Food food = new Food(
                1L,
                "123456789",
                "Apple",
                "Fresh Farm",
                "FRUIT",
                1L
             );

             when(catalog.byBarcode("123456789"))
                .thenReturn(Optional.of(food));

             mockMvc.perform(get("/api/v1/foods/barcode/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("123456789"))
                .andExpect(jsonPath("$.name").value("Apple"));

      }
      @Test
      void shouldReturnFoodsMatchingSearch() throws Exception {

             Food apple = new Food(
                1L,
                "111",
                "Apple",
                "Fresh Farm",
                "FRUIT",
                1L
             );

              Food banana = new Food(
                2L,
                "222",
                "Banana",
                "Fresh Farm",
                "FRUIT",
                1L
             );

             when(catalog.search("a", "FRUIT", 2))
                .thenReturn(List.of(apple, banana));

             mockMvc.perform(get("/api/v1/foods/search")
                .param("q", "a")
                .param("category", "FRUIT")
                .param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));




      }



}
