package com.reduceco2now.ingestion.internal.off;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Talks to the Open Food Facts "search" API (v2) to fetch a single batch of
 * products.
 *
 * <p>This does not implement pagination/full sync — it fetches one page at a
 * time using {@code page} and {@code page_size}. Callers wanting more data can
 * call {@link #fetchBatch(int)} again with the next page number.
 *
 * <p>Only the fields we actually map are requested (via the {@code fields}
 * query param) to keep responses small.
 */
public final class OpenFoodFactsClient {

    private static final Logger log = LoggerFactory.getLogger(OpenFoodFactsClient.class);

    /** Default number of products requested per call. */
    public static final int DEFAULT_PAGE_SIZE = 100;

    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v2/search";

    private static final List<String> REQUESTED_FIELDS = List.of(
            "code",
            "product_name",
            "brands",
            "categories_tags",
            "quantity",
            "image_url"
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

    /**
     * Fetches the first page of products using the configured page size.
     */
    public List<JsonNode> fetchBatch() {
        return fetchBatch(1);
    }

    /**
     * Fetches a single page of products.
     *
     * @param page 1-based page number.
     * @return the raw {@code product} JSON nodes for this page. Never null;
     *         empty if the response had no {@code products} array or the call failed.
     */
    public List<JsonNode> fetchBatch(int page) {
        String url = buildUrl(page);
        String body;
        try {
            body = transport.get(url);
        } catch (IOException e) {
            log.error("Failed to fetch OFF batch (page={}) due to a network error", page, e);
            return List.of();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while fetching OFF batch (page={})", page, e);
            return List.of();
        } catch (OffApiException e) {
            log.error("OFF API returned an error status for page={}: {}", page, e.getMessage());
            return List.of();
        }

        JsonNode root;
        try {
            root = objectMapper.readTree(body);
        } catch (IOException e) {
            log.error("OFF response for page={} was not valid JSON; skipping batch", page, e);
            return List.of();
        }

        if (root == null || root.isNull() || !root.isObject()) {
            log.warn("OFF response for page={} was not a JSON object; skipping batch", page);
            return List.of();
        }

        JsonNode products = root.get("products");
        if (products == null || !products.isArray()) {
            log.warn("OFF response for page={} had no 'products' array; skipping batch", page);
            return List.of();
        }

        List<JsonNode> result = new ArrayList<>(products.size());
        products.forEach(result::add);
        return result;
    }

    private String buildUrl(int page) {
        StringJoiner fields = new StringJoiner(",");
        REQUESTED_FIELDS.forEach(fields::add);

        return BASE_URL
                + "?page=" + page
                + "&page_size=" + pageSize
                + "&fields=" + urlEncode(fields.toString());
    }

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
