package com.reduceco2now.ingestion.internal.off;

import java.io.IOException;

/**
 * Minimal HTTP GET abstraction used by {@link OpenFoodFactsClient}.
 *
 * <p>Exists purely so tests can supply a canned response without hitting the
 * real Open Food Facts API. The production implementation is
 * {@link JdkOffHttpTransport}.
 */
public interface OffHttpTransport {

    /**
     * Performs an HTTP GET against the given URL.
     *
     * @param url full request URL, including query string.
     * @return the raw response body as a string.
     * @throws IOException              on network failure.
     * @throws OffApiException          if the server responds with a non-2xx status.
     * @throws InterruptedException     if the calling thread is interrupted mid-request.
     */
    String get(String url) throws IOException, InterruptedException;
}
