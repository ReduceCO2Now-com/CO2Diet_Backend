package com.reduceco2now.ingestion.internal.off;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenFoodFactsClientTest {

    @Mock
    private OffHttpTransport transport;

    @Test
    void fetchBatchParsesProductsFromAMockedResponse() throws Exception {
        String json = """
                {
                  "count": 2,
                  "page": 1,
                  "page_size": 100,
                  "products": [
                    {"code": "111", "product_name": "Product One"},
                    {"code": "222", "product_name": "Product Two"}
                  ]
                }
                """;
        when(transport.get(anyString())).thenReturn(json);

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        List<JsonNode> products = client.fetchBatch();

        assertThat(products).hasSize(2);
        assertThat(products.get(0).get("code").asText()).isEqualTo("111");
        assertThat(products.get(1).get("code").asText()).isEqualTo("222");
    }

    @Test
    void requestUrlIncludesPageSizeAndFields() throws Exception {
        when(transport.get(anyString())).thenReturn("""
                {"products": []}
                """);

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        client.fetchBatch();

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(transport).get(urlCaptor.capture());
        String url = urlCaptor.getValue();

        assertThat(url).contains("page_size=" + OpenFoodFactsClient.DEFAULT_PAGE_SIZE);
        assertThat(url).contains("page=1");
        assertThat(url).contains("fields=");
        assertThat(url).contains("world.openfoodfacts.org/api/v2/search");
    }

    @Test
    void returnsEmptyListWhenProductsFieldIsMissing() throws Exception {
        when(transport.get(anyString())).thenReturn("""
                {"count": 0}
                """);

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        List<JsonNode> products = client.fetchBatch();

        assertThat(products).isEmpty();
    }

    @Test
    void returnsEmptyListWhenResponseIsNotValidJson() throws Exception {
        when(transport.get(anyString())).thenReturn("not json at all {{{");

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        List<JsonNode> products = client.fetchBatch();

        assertThat(products).isEmpty();
    }

    @Test
    void returnsEmptyListWhenResponseIsJsonNull() throws Exception {
        when(transport.get(anyString())).thenReturn("null");

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);

        assertThat(client.fetchBatch()).isEmpty();
    }

    @Test
    void returnsEmptyListWhenTransportThrowsIoException() throws Exception {
        when(transport.get(anyString())).thenThrow(new java.io.IOException("network down"));

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        List<JsonNode> products = client.fetchBatch();

        assertThat(products).isEmpty();
    }

    @Test
    void returnsEmptyListWhenTransportThrowsOffApiException() throws Exception {
        when(transport.get(anyString())).thenThrow(new OffApiException(503, "service unavailable"));

        OpenFoodFactsClient client = new OpenFoodFactsClient(transport);
        List<JsonNode> products = client.fetchBatch();

        assertThat(products).isEmpty();
    }
}
