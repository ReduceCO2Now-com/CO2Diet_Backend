package com.reduceco2now.ingestion;

import java.util.List;

/**
 * A pluggable upstream source of food product data for ingestion.
 *
 * <p>Implementations are responsible for talking to their upstream API/format
 * and returning already-normalized {@link FoodUpsert} records. Any per-record
 * failures (malformed entries, missing required fields, etc.) should be
 * handled internally — skip and log the bad record rather than throwing and
 * failing the whole batch.
 */
public interface FoodSource {

    /**
     * @return a short, stable identifier for this source, e.g. {@code "off"}.
     */
    String name();

    /**
     * Fetches a single batch of products from the upstream source.
     *
     * <p>This does not guarantee a full sync — callers wanting the entire
     * catalog should call this repeatedly with source-specific pagination
     * support once that exists.
     *
     * @return the products in this batch, mapped to our normalized shape.
     *         Malformed upstream entries are skipped rather than included.
     */
    List<FoodUpsert> fetchBatch();
}
