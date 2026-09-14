package com.reduceco2now.ingestion.internal;

import com.reduceco2now.ingestion.internal.off.OffFoodSource;
import com.reduceco2now.ingestion.internal.usda.UsdaClient;
import com.reduceco2now.ingestion.internal.usda.UsdaFoodMapper;
import com.reduceco2now.ingestion.internal.usda.UsdaFoodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IngestionConfig {

    @Bean
    UsdaClient usdaClient(@Value("${usda.api.key}") String usdaApiKey) {
        return new UsdaClient(usdaApiKey);
    }

    @Bean
    UsdaFoodMapper usdaFoodMapper() {
        return new UsdaFoodMapper();
    }

    @Bean
    UsdaFoodSource usdaFoodSource(UsdaClient usdaClient, UsdaFoodMapper usdaFoodMapper) {
        return new UsdaFoodSource(usdaClient, usdaFoodMapper);
    }

    @Bean
    OffFoodSource offFoodSource(
            @Value("${off.app-name}") String appName,
            @Value("${off.app-version}") String appVersion,
            @Value("${off.contact-email}") String contactEmail) {
        return OffFoodSource.createDefault(appName, appVersion, contactEmail);
    }
}