package com.reduceco2now.ingestion.internal;

import com.reduceco2now.catalog.FoodUpsert;
import com.reduceco2now.ingestion.FoodSource;
import com.reduceco2now.ingestion.internal.off.OffFoodSource;
import com.reduceco2now.ingestion.internal.off.OffHttpTransport;
import com.reduceco2now.ingestion.internal.off.OffProductMapper;
import com.reduceco2now.ingestion.internal.off.OpenFoodFactsClient;
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
    void fetchFoodsReturnsMappedUpsertsAndSkipsMalformedEntries() throws Exception {
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

        List<FoodUpsert> result = source.fetchFoods();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).barcode()).isEqualTo("1");
        assertThat(result.get(0).brand()).isEqualTo("Acme");
    }
}