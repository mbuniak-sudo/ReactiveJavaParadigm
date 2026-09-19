package com.griddynamics.reactive.course.userservice.integration;

import com.griddynamics.reactive.course.userservice.integration.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.time.Duration;

@Slf4j
@Component
public class ProductInfoSearchServiceClient {

    private final WebClient webClient;

    public ProductInfoSearchServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${product-search-service.base-url:https://localhost:8082}") String baseUrl) {
        webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_NDJSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().responseTimeout(Duration.ofSeconds(5))))
                .build();
    }


    public Flux<ProductDto> getProducts(String productCode) {
        return webClient.get()
                .uri("/productInfoService/product/names/{productCode}", productCode)
                .exchangeToFlux(response -> response.bodyToFlux(ProductDto.class))
                .doOnNext(productDto ->
                        log.info("Successfully searched for products by product code {} : {}",
                                productCode, productDto))
                .onErrorResume(e -> {
                    log.error("Error while fetching products by product code {}: {}", productCode, e.getMessage());

                    return Flux.empty();
                });
    }

    public Mono<ProductDto> getProductWithHighestScore(String productCode) {
        return getProducts(productCode)
                .reduce((p1, p2) -> p1.getScore() >= p2.getScore() ? p1 : p2)
                .onErrorResume(e -> {
                    log.error("Error selecting highest-score product: {}", e.getMessage());

                    return Mono.empty();
                });
    }
}
