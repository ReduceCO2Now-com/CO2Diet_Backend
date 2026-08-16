package com.reduceco2now.ingestion.internal.off;

import com.reduceco2now.ingestion.FoodSource;
import com.reduceco2now.ingestion.FoodUpsert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OffFoodSourceTest {

    @Mock
    private OffHttpTransport transport;

    @Test
    void fetchBatchReturnsMappedUpsertsAndSkipsMalformedEntries() throws Exception {
        String json = """
                {
                  "products": [
                    {"code": "1", "product_name": "Good Product", "brands": "Acme"},
                    {"product_name": "Missing code - should be skipped"},
                    {"code": "2"}
                  ]
                }
                """;
        when(transport.get(anyString())).thenReturn(json);

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        FoodSource source = new OffFoodSource(client, new OffProductMapper());

        List<FoodUpsert> result = source.fetchBatch();

        assertThat(source.name()).isEqualTo("off");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).externalId()).isEqualTo("1");
        assertThat(result.get(0).brand()).isEqualTo("Acme");
    }
}
