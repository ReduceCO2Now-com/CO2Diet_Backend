package com.reduceco2now.catalog;

/** Write operations on the catalog. Injected by modules like ingestion/contributions. */
public interface CatalogCommand {

    /** Checks if the barcode already exists: updates it (bumps rowVersion) if so, creates it if not. */
    Food upsert(FoodUpsert upsert);
}