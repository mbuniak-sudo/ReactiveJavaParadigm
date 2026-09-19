package com.griddynamics.reactive.course.userservice.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.griddynamics.reactive.course.userservice.integration.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductInfoSearchServiceClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();


    @Test
    void shouldReturnProducts() {
        // given
        String productCode = "IPHONE-15";

        String responseBody =
                "{\"productCode\":\"IPHONE-15\",\"score\":0.85}\n" +
                        "{\"productCode\":\"IPHONE-15\",\"score\":0.95}\n";

        wireMock.stubFor(
                get(urlEqualTo(
                        "/productInfoService/product/names/" + productCode))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/x-ndjson")
                                        .withBody(responseBody)
                        )
        );

        ProductInfoSearchServiceClient client = createClient();

        // when
        Flux<ProductDto> products = client.getProducts(productCode);

        // then
        StepVerifier.create(products)
                .assertNext(product -> {
                    assertEquals("IPHONE-15", product.getProductCode());
                    assertEquals(0.85, product.getScore(), 0.001);
                })
                .assertNext(product -> {
                    assertEquals("IPHONE-15", product.getProductCode());
                    assertEquals(0.95, product.getScore(), 0.001);
                })
                .verifyComplete();

        wireMock.verify(
                getRequestedFor(
                        urlEqualTo(
                                "/productInfoService/product/names/" + productCode))
        );
    }

    @Test
    void shouldReturnEmptyFluxWhenServerReturns500() {
        // given
        String productCode = "IPHONE-15";

        wireMock.stubFor(
                get(urlEqualTo(
                        "/productInfoService/product/names/" + productCode))
                        .willReturn(
                                aResponse()
                                        .withStatus(500)
                        )
        );

        ProductInfoSearchServiceClient client = createClient();

        // when / then
        StepVerifier.create(client.getProducts(productCode))
                .verifyComplete();

        wireMock.verify(
                getRequestedFor(
                        urlEqualTo(
                                "/productInfoService/product/names/" + productCode))
        );
    }

    @Test
    void shouldReturnEmptyFluxWhenResponseContainsInvalidJson() {
        // given
        String productCode = "IPHONE-15";

        wireMock.stubFor(
                get(urlEqualTo(
                        "/productInfoService/product/names/" + productCode))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/x-ndjson")
                                        .withBody(
                                                "{\"productCode\":\"IPHONE-15\",\"score\":\n"
                                        )
                        )
        );

        ProductInfoSearchServiceClient client = createClient();

        // when / then
        StepVerifier.create(client.getProducts(productCode))
                .verifyComplete();
    }

    @Test
    void shouldSendCorrectRequest() {
        // given
        String productCode = "IPHONE-15";

        wireMock.stubFor(
                get(urlEqualTo(
                        "/productInfoService/product/names/" + productCode))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/x-ndjson")
                                        .withBody(
                                                "{\"productCode\":\"IPHONE-15\",\"score\":0.95}\n"
                                        )
                        )
        );

        ProductInfoSearchServiceClient client = createClient();

        // when
        StepVerifier.create(client.getProducts(productCode))
                .expectNextCount(1)
                .verifyComplete();

        // then
        wireMock.verify(
                getRequestedFor(
                        urlEqualTo(
                                "/productInfoService/product/names/" + productCode))
                        .withHeader(
                                "Content-Type",
                                equalTo("application/x-ndjson"))
        );
    }

    @Test
    void shouldReturnProductWithHighestScore() {
        // given
        String productCode = "IPHONE-15";

        String responseBody =
                "{\"productCode\":\"IPHONE-15\",\"score\":0.75}\n" +
                        "{\"productCode\":\"IPHONE-15\",\"score\":0.95}\n" +
                        "{\"productCode\":\"IPHONE-15\",\"score\":0.85}\n";

        wireMock.stubFor(
                get(urlEqualTo(
                        "/productInfoService/product/names/" + productCode))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/x-ndjson")
                                        .withBody(responseBody)
                        )
        );

        ProductInfoSearchServiceClient client = createClient();

        // when / then
        StepVerifier.create(
                        client.getProductWithHighestScore(productCode))
                .assertNext(product -> {
                    assertEquals("IPHONE-15", product.getProductCode());
                    assertEquals(0.95, product.getScore(), 0.001);
                })
                .verifyComplete();

        wireMock.verify(
                getRequestedFor(
                        urlEqualTo(
                                "/productInfoService/product/names/" + productCode))
        );
    }

    @Test
    void shouldReturnEmptyMonoWhenServerReturns500() {
        // given
        String productCode = "IPHONE-15";

        wireMock.stubFor(
                get(urlEqualTo(
                        "/productInfoService/product/names/" + productCode))
                        .willReturn(
                                aResponse()
                                        .withStatus(500)
                        )
        );

        ProductInfoSearchServiceClient client = createClient();

        // when / then
        StepVerifier.create(
                        client.getProductWithHighestScore(productCode))
                .verifyComplete();
    }

    private ProductInfoSearchServiceClient createClient() {
        WebClient.Builder webClientBuilder = WebClient.builder();

        return new ProductInfoSearchServiceClient(
                webClientBuilder,
                wireMock.baseUrl()
        );
    }
}
