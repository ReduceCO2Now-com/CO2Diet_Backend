package com.reduceco2now.ingestion.internal.off;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.FoodSource;

import java.util.List;
import java.util.Objects;

public final class OffFoodSource implements FoodSource {

    private final OpenFoodFactsClient client;
    private final OffProductMapper mapper;

    public OffFoodSource(OpenFoodFactsClient client, OffProductMapper mapper) {
        this.client = Objects.requireNonNull(client, "client");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    public static OffFoodSource createDefault(String appName, String appVersion, String contactEmail) {
        OffHttpTransport transport = new JdkOffHttpTransport(appName, appVersion, contactEmail);
        return new OffFoodSource(new OpenFoodFactsClient(transport), new OffProductMapper());
    }

    @Override
    public List<FoodUpsert> fetchFoods() {
        return mapper.mapBatch(client.fetchBatch());
    }
}
