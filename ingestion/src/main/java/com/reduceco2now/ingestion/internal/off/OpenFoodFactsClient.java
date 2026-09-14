package com.reduceco2now.ingestion.internal.off;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public final class OpenFoodFactsClient {

    public static final int DEFAULT_PAGE_SIZE = 100;
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v2/search";
    private static final List<String> REQUESTED_FIELDS = List.of(
            "code", "product_name", "brands", "categories_tags", "quantity", "image_url"
    );

    private final OffHttpTransport transport;
    private final ObjectMapper objectMapper;
    private final int pageSize;

    public OpenFoodFactsClient(OffHttpTransport transport) {
        this(transport, new ObjectMapper(), DEFAULT_PAGE_SIZE);
    }

    public OpenFoodFactsClient(OffHttpTransport transport, ObjectMapper objectMapper, int pageSize) {
        this.transport = Objects.requireNonNull(transport, "transport");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be positive, was " + pageSize);
        }
        this.pageSize = pageSize;
    }

    public List<JsonNode> fetchBatch() {
        return fetchBatch(1);
    }

    public List<JsonNode> fetchBatch(int page) {
        String url = buildUrl(page);
        String body;
        try {
            body = transport.get(url);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to fetch OFF batch (page=" + page + ")", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while fetching OFF batch (page=" + page + ")", e);
        }

        JsonNode root;
        try {
            root = objectMapper.readTree(body);
        } catch (IOException e) {
            throw new UncheckedIOException("OFF response for page=" + page + " was not valid JSON", e);
        }

        if (root == null || root.isNull() || !root.isObject()) {
            throw new IllegalStateException("OFF response for page=" + page + " was not a JSON object");
        }
        JsonNode products = root.get("products");
        if (products == null || !products.isArray()) {
            throw new IllegalStateException("OFF response for page=" + page + " had no 'products' array");
        }

        List<JsonNode> result = new ArrayList<>(products.size());
        products.forEach(result::add);
        return result;
    }

    private String buildUrl(int page) {
        StringJoiner fields = new StringJoiner(",");
        REQUESTED_FIELDS.forEach(fields::add);
        return BASE_URL + "?page=" + page + "&page_size=" + pageSize + "&fields=" + urlEncode(fields.toString());
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}