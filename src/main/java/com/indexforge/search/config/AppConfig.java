package com.indexforge.search.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

/**
 * Application configuration for IndexForge Search.
 * Configures the WebClient used to communicate with the embedding service.
 */
@Configuration
public class AppConfig {

    @Value("${embedding.service.base-url}")
    private String embeddingServiceBaseUrl;

    @Bean
    public WebClient embeddingServiceWebClient() {
        return WebClient.builder()
                .baseUrl(embeddingServiceBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}