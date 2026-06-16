package com.indexforge.search.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Real implementation of QueryEmbeddingService that calls the FastAPI embedding service.
 *
 * Endpoint: POST http://localhost:8000/api/v1/embed
 * Request:  {"text": "<query>"}
 * Response: {"embedding": [0.012, -0.034, ...]}  (384 floats)
 */
@Service
@Slf4j
public class HttpQueryEmbeddingService implements QueryEmbeddingService {

    private static final int EXPECTED_DIMENSION = 384;

    private final WebClient embeddingServiceWebClient;
    private final Duration timeout;

    public HttpQueryEmbeddingService(
            WebClient embeddingServiceWebClient,
            @Value("${embedding.service.timeout-seconds:30}") int timeoutSeconds) {
        this.embeddingServiceWebClient = embeddingServiceWebClient;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
    }

    /**
     * Calls the FastAPI embedding service to generate a 384-dimensional embedding vector.
     *
     * @param query the natural-language search query
     * @return float array of dimension 384
     * @throws EmbeddingGenerationException on timeout, invalid response, or service failure
     */
    @Override
    public float[] generateEmbedding(String query) {
        log.info("Generating embedding for query: '{}'", query);
        long startTime = System.currentTimeMillis();

        try {
            // Call the FastAPI embedding service
            Map<String, Object> response = embeddingServiceWebClient.post()
                    .uri("/api/v1/embed")
                    .bodyValue(Map.of("text", query))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(timeout)
                    .block();

            long latencyMs = System.currentTimeMillis() - startTime;

            if (response == null || !response.containsKey("embedding")) {
                throw new EmbeddingGenerationException(
                        "Invalid response from embedding service: missing 'embedding' field");
            }

            // Parse the embedding from the response
            Object embeddingObj = response.get("embedding");
            if (!(embeddingObj instanceof List<?> embeddingList)) {
                throw new EmbeddingGenerationException(
                        "Invalid response from embedding service: 'embedding' is not a list");
            }

            // Convert List<Number> to float[]
            float[] embedding = new float[embeddingList.size()];
            for (int i = 0; i < embeddingList.size(); i++) {
                Object val = embeddingList.get(i);
                if (val instanceof Number number) {
                    embedding[i] = number.floatValue();
                } else {
                    throw new EmbeddingGenerationException(
                            "Invalid embedding value at index " + i + ": " + val);
                }
            }

            // Validate dimension
            if (embedding.length != EXPECTED_DIMENSION) {
                throw new EmbeddingGenerationException(
                        "Embedding dimension mismatch: expected " + EXPECTED_DIMENSION +
                        " but got " + embedding.length);
            }

            log.info("Embedding generated successfully | dimension={} | latency={}ms",
                    embedding.length, latencyMs);

            return embedding;

        } catch (WebClientResponseException e) {
            long latencyMs = System.currentTimeMillis() - startTime;
            log.error("Embedding service returned error | status={} | latency={}ms | body={}",
                    e.getStatusCode(), latencyMs, e.getResponseBodyAsString());
            throw new EmbeddingGenerationException(
                    "Embedding service error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (WebClientRequestException e) {
            long latencyMs = System.currentTimeMillis() - startTime;
            log.error("Cannot reach embedding service | latency={}ms | error={}", latencyMs, e.getMessage());
            throw new EmbeddingGenerationException(
                    "Cannot reach embedding service at configured URL: " + e.getMessage(), e);

        } catch (EmbeddingGenerationException e) {
            throw e;

        } catch (Exception e) {
            long latencyMs = System.currentTimeMillis() - startTime;
            log.error("Unexpected error generating embedding | latency={}ms | error={}", latencyMs, e.getMessage());
            throw new EmbeddingGenerationException(
                    "Unexpected error generating embedding: " + e.getMessage(), e);
        }
    }
}