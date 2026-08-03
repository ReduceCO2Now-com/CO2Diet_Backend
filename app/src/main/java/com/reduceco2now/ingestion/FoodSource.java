package com.reduceco2now.ingestion;

import com.reduceco2now.catalog.FoodUpsert;

import java.util.List;

// Represents an external food provider.

public interface FoodSource {

    /**
     * Fetches a batch of foods from the external source.
     *
     * @return foods ready to be imported into the catalog
     */
    List<FoodUpsert> fetchFoods();
}