package com.reduceco2now.ingestion.internal.off;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

/**
 * Production {@link OffHttpTransport} backed by the JDK's {@link HttpClient}.
 *
 * <p>Open Food Facts requires a custom User-Agent of the form
 * {@code AppName/Version (ContactEmail)} on every request so they can identify
 * traffic and avoid mistaking it for a bot — see
 * https://openfoodfacts.github.io/openfoodfacts-server/api/. This class builds
 * that header once and reuses it for every request.
 */
public final class JdkOffHttpTransport implements OffHttpTransport {

    private final HttpClient httpClient;
    private final String userAgent;

    public JdkOffHttpTransport(String appName, String appVersion, String contactEmail) {
        this(HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(10))
                        .build(),
                buildUserAgent(appName, appVersion, contactEmail));
    }

    JdkOffHttpTransport(HttpClient httpClient, String userAgent) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.userAgent = Objects.requireNonNull(userAgent, "userAgent");
    }

    static String buildUserAgent(String appName, String appVersion, String contactEmail) {
        Objects.requireNonNull(appName, "appName");
        Objects.requireNonNull(appVersion, "appVersion");
        Objects.requireNonNull(contactEmail, "contactEmail");
        return "%s/%s (%s)".formatted(appName, appVersion, contactEmail);
    }

    @Override
    public String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", userAgent)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new OffApiException(status, "OFF API request failed with status " + status + " for " + url);
        }
        return response.body();
    }
}
