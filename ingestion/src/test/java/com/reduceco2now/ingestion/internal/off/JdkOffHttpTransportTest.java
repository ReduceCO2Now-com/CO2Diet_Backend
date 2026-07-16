package com.reduceco2now.ingestion.internal.off;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JdkOffHttpTransportTest {

    @Test
    void buildsUserAgentInTheFormatOffRequires() {
        // OFF requires "AppName/Version (ContactEmail)" — see
        // https://openfoodfacts.github.io/openfoodfacts-server/api/
        String userAgent = JdkOffHttpTransport.buildUserAgent("ReduceCO2Now", "0.1.0", "ingestion@reduceco2now.com");

        assertThat(userAgent).isEqualTo("ReduceCO2Now/0.1.0 (ingestion@reduceco2now.com)");
    }
}
