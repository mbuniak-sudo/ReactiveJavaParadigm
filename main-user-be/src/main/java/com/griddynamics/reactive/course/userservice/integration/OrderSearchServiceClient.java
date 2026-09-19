package com.griddynamics.reactive.course.userservice.integration;

import com.griddynamics.reactive.course.userservice.integration.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.time.Duration;

@Slf4j
@Component
public class OrderSearchServiceClient {

    private final WebClient webClient;

    public OrderSearchServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${order-search-service.base-url:https://localhost:8081}") String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_NDJSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().responseTimeout(Duration.ofSeconds(5))))
                .build();
    }


    public Flux<OrderDto> getOrders(String phoneNumber) {
        return webClient.get()
                .uri("/orderSearchService/order/{phoneNumber}",phoneNumber)
                .exchangeToFlux(response -> response.bodyToFlux(OrderDto.class))
                .doOnNext(orderDto ->
                        log.info("Successfully searched for orders by phone number {} : {}",
                                orderDto.getProductCode(), orderDto))
                .onErrorResume(e -> {
                    log.error("Error while fetching orders by phone number {}: {}", phoneNumber, e.getMessage());

                    return Flux.empty();
                });
    }
}
