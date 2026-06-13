package com.indexforge.search.config;

import org.springframework.context.annotation.Configuration;

/**
 * Application configuration.
 *
 * TODO: Add beans for:
 *   - RestTemplate or WebClient for embedding provider API calls
 *   - Embedding provider configuration (API key, model, dimension)
 *   - Search configuration (default result limit, min similarity threshold)
 */
@Configuration
public class AppConfig {

    // TODO: Configure HTTP client for embedding provider
    // @Bean
    // public RestTemplate restTemplate() {
    //     return new RestTemplateBuilder()
    //             .setConnectTimeout(Duration.ofSeconds(10))
    //             .setReadTimeout(Duration.ofSeconds(30))
    //             .build();
    // }
}