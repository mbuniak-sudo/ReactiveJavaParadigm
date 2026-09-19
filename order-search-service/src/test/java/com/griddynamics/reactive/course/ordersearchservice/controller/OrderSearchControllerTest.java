package com.griddynamics.reactive.course.ordersearchservice.controller;

import com.griddynamics.reactive.course.ordersearchservice.domain.Order;
import com.griddynamics.reactive.course.ordersearchservice.service.OrderSearchService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class OrderSearchControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @MockBean
    OrderSearchService orderSearchService;


    @Test
    public void shouldReturnOrdersWhenGetOrdersByPhoneCalls() {
        List<Order> orders = List.of(
                new Order("+123", "1", "1313"),
                new Order("+321", "2", "1010"),
                new Order("+789", "3", "1212"),
                new Order("+987", "11", "1414"));

        Mockito.when(orderSearchService.getOrdersByPhone("123"))
                .thenReturn(Flux.fromIterable(orders));

        StepVerifier.create(webTestClient.get()
                        .uri(builder -> builder
                                .path("/orderSearchService/order/{phoneNumber}")
                                .build("123"))
                        .accept(MediaType.APPLICATION_NDJSON)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_NDJSON_VALUE)
                        .returnResult(Order.class)
                        .getResponseBody())
                .expectNextCount(4)
                .verifyComplete();

        Mockito.verify(orderSearchService, Mockito.times(1)).getOrdersByPhone("123");
    }
}