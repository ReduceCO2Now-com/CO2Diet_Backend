package com.reduceco2now.ingestion.internal.off;

import com.fasterxml.jackson.databind.JsonNode;
import com.reduceco2now.ingestion.FoodSource;
import com.reduceco2now.ingestion.FoodUpsert;

import java.util.List;
import java.util.Objects;

/**
 * {@link FoodSource} implementation backed by Open Food Facts.
 *
 * <p>Wraps {@link OpenFoodFactsClient} (HTTP + raw JSON) and
 * {@link OffProductMapper} (JSON -&gt; {@link FoodUpsert}) behind the shared
 * ingestion interface. Does not yet support paginating through the full
 * catalog — {@link #fetchBatch()} returns a single page.
 */
public final class OffFoodSource implements FoodSource {

    public static final String NAME = OffProductMapper.SOURCE_NAME;

    private final OpenFoodFactsClient client;
    private final OffProductMapper mapper;

    public OffFoodSource(OpenFoodFactsClient client, OffProductMapper mapper) {
        this.client = Objects.requireNonNull(client, "client");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    /**
     * Convenience factory wiring up a real HTTP transport and default client/mapper.
     *
     * @param appName      your application's name, used in the required OFF User-Agent.
     * @param appVersion   your application's version, used in the required OFF User-Agent.
     * @param contactEmail contact email, used in the required OFF User-Agent.
     */
    public static OffFoodSource createDefault(String appName, String appVersion, String contactEmail) {
        OffHttpTransport transport = new JdkOffHttpTransport(appName, appVersion, contactEmail);
        return new OffFoodSource(new OpenFoodFactsClient(transport), new OffProductMapper());
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public List<FoodUpsert> fetchBatch() {
        List<JsonNode> rawProducts = client.fetchBatch();
        return mapper.mapBatch(rawProducts);
    }
}
