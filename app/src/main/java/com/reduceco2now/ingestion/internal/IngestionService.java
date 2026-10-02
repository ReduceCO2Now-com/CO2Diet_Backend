package com.reduceco2now.ingestion.internal;

import com.reduceco2now.catalog.CatalogCommand;
import com.reduceco2now.catalog.Food;
import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.FoodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngestionService {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);

    private final List<FoodSource> foodSources;
    private final CatalogCommand catalogCommand;

    public IngestionService(List<FoodSource> foodSources, CatalogCommand catalogCommand) {
        this.foodSources = foodSources;
        this.catalogCommand = catalogCommand;
    }

    public void runIngestion() {
        log.info("Starting ingestion run with {} registered sources.", foodSources.size());

        int created = 0;
        int updated = 0;
        int skipped = 0;
        int failed = 0;

        for (FoodSource source : foodSources) {
            String sourceName = source.getClass().getSimpleName();
            log.info("Fetching data from source: {}", sourceName);

            try {
                List<FoodUpsert> items = source.fetchFoods();

                if (items == null) {
                    log.warn("Source {} returned no items.", sourceName);
                    continue;
                }

                for (FoodUpsert item : items) {
                    if (item == null) {
                        skipped++;
                        continue;
                    }

                    try {
                        Food saved = catalogCommand.upsert(item);

                        if (saved.rowVersion() <= 1L) {
                            created++;
                        } else {
                            updated++;
                        }
                    } catch (Exception e) {
                        failed++;
                        log.error("Failed to upsert item with barcode: {}", item.barcode(), e);
                    }
                }
            } catch (Exception e) {
                failed++;
                log.error("Source {} failed during fetch operation.", sourceName, e);
            }
        }

        log.info(
                "Ingestion run complete. created={}, updated={}, skipped={}, failed={}",
                created,
                updated,
                skipped,
                failed);
    }
}