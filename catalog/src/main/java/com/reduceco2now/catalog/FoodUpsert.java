package com.reduceco2now.catalog;


/** Input shape used by modules like ingestion/contributions to submit new food data. */
public record FoodUpsert(
        String barcode,
        String name,
        String brand,
        String categoryCode
) {}