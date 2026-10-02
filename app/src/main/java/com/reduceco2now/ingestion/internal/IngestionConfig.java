package com.reduceco2now.ingestion.internal;

import com.reduceco2now.ingestion.internal.usda.UsdaClient;
import com.reduceco2now.ingestion.internal.usda.UsdaFoodMapper;
import com.reduceco2now.ingestion.internal.usda.UsdaFoodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers each FoodSource implementation as a Spring bean so IngestionService
 * can
 * receive them via {@code List<FoodSource>}.
 *
 * UsdaClient/UsdaFoodMapper/UsdaFoodSource are plain classes (no @Component) by
 * design,
 * so this is the one place that wires them up instead of editing Task 2's
 * files.
 * Add the OFF equivalent here once Task 1 lands.
 */
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
}
