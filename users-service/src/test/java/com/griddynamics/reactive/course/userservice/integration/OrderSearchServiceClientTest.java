package com.griddynamics.reactive.course.userservice.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class OrderSearchServiceClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private OrderSearchServiceClient createClient() {
        WebClient.Builder webClientBuilder = WebClient.builder();

        return new OrderSearchServiceClient(webClientBuilder, wireMock.baseUrl());
    }


    @Test
    void shouldReturnOrdersWhenOrderSearchServiceRespondsSuccessfully() {
        String phoneNumber = "48123456789";
        String responseBody =
                "{\"productCode\":\"IPHONE-15\",\"orderNumber\":1}\n" +
                        "{\"productCode\":\"MACBOOK-PRO\",\"orderNumber\":2}\n";

        wireMock.stubFor(get(urlEqualTo("/orderSearchService/order/" + phoneNumber))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/x-ndjson")
                        .withBody(responseBody)));
        OrderSearchServiceClient client = createClient();

        StepVerifier.create(client.getOrders(phoneNumber))
                .expectNextMatches(order -> "IPHONE-15".equals(order.getProductCode()))
                .expectNextMatches(order -> "MACBOOK-PRO".equals(order.getProductCode()))
                .verifyComplete();

        wireMock.verify(getRequestedFor(urlEqualTo("/orderSearchService/order/" + phoneNumber)));
    }

    @Test
    void shouldReturnEmptyFluxWhenOrderSearchServiceReturnsError() {
        String phoneNumber = "48123456789";

        wireMock.stubFor(get(urlEqualTo("/orderSearchService/order/" + phoneNumber))
                .willReturn(aResponse().withStatus(500)));
        OrderSearchServiceClient client = createClient();

        StepVerifier.create(client.getOrders(phoneNumber)).verifyComplete();

        wireMock.verify(getRequestedFor(urlEqualTo("/orderSearchService/order/" + phoneNumber)));
    }

    @Test
    void shouldReturnEmptyFluxWhenOrderSearchServiceIsUnavailable() {
        String phoneNumber = "48123456789";

        wireMock.stubFor(get(urlEqualTo("/orderSearchService/order/" + phoneNumber))
                .willReturn(aResponse().withStatus(503)));
        OrderSearchServiceClient client = createClient();

        StepVerifier.create(client.getOrders(phoneNumber)).verifyComplete();
    }

    @Test
    void shouldSendCorrectRequestHeaders() {
        String phoneNumber = "48123456789";

        wireMock.stubFor(get(urlEqualTo("/orderSearchService/order/" + phoneNumber))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/x-ndjson")
                        .withBody("{\"productCode\":\"IPHONE-15\",\"quantity\":1}")));

        OrderSearchServiceClient client = createClient(); // when
        StepVerifier.create(client.getOrders(phoneNumber)).expectNextCount(1).verifyComplete();

        wireMock.verify(getRequestedFor(urlEqualTo("/orderSearchService/order/" + phoneNumber))
                .withHeader("Content-Type", equalTo("application/x-ndjson")));
    }
}
