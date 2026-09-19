package com.griddynamics.reactive.course.userservice.service.impl;

import com.griddynamics.reactive.course.userservice.db.entity.UserEntity;
import com.griddynamics.reactive.course.userservice.db.repo.UsersRepository;
import com.griddynamics.reactive.course.userservice.domain.OrderProductInfo;
import com.griddynamics.reactive.course.userservice.integration.OrderSearchServiceClient;
import com.griddynamics.reactive.course.userservice.integration.ProductInfoSearchServiceClient;
import com.griddynamics.reactive.course.userservice.integration.dto.OrderDto;
import com.griddynamics.reactive.course.userservice.integration.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UsersRepository repository;
    @Mock
    ProductInfoSearchServiceClient productClient;
    @Mock
    OrderSearchServiceClient orderSearchServiceClient;
    @InjectMocks
    UserServiceImpl service;


    @Test
    void shouldReturnOrderProductInfo() {
        // given
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.set_id(userId);
        user.setName("John Smith");
        user.setPhone("48123456789");

        OrderDto order = new OrderDto();
        order.setOrderNumber("ORDER-123");
        order.setProductCode("IPHONE-15");

        ProductDto product = new ProductDto();
        product.setProductName("iPhone 15");
        product.setScore(0.95);

        when(repository.findById(userId))
                .thenReturn(Mono.just(user));

        when(orderSearchServiceClient.getOrders(user.getPhone()))
                .thenReturn(Flux.just(order));

        when(productClient.getProductWithHighestScore(order.getProductCode()))
                .thenReturn(Mono.just(product));

        // when
        Flux<OrderProductInfo> result = service.getBy(userId);

        // then
        StepVerifier.create(result)
                .assertNext(orderProductInfo -> {
                    assertEquals("John Smith", orderProductInfo.getUserName());
                    assertEquals("48123456789",
                            orderProductInfo.getPhoneNumber());
                    assertEquals("ORDER-123",
                            orderProductInfo.getOrderNumber());
                    assertEquals("IPHONE-15",
                            orderProductInfo.getProductCode());
                    assertEquals("iPhone 15",
                            orderProductInfo.getProductName());
                    assertEquals(
                            BigDecimal.valueOf(0.95),
                            orderProductInfo.getScore()
                    );
                })
                .verifyComplete();

        verify(repository).findById(userId);
        verify(orderSearchServiceClient).getOrders(user.getPhone());
        verify(productClient)
                .getProductWithHighestScore(order.getProductCode());
    }

    @Test
    void shouldReturnEmptyFluxWhenUserDoesNotExist() {
        // given
        Long userId = 1L;

        when(repository.findById(userId))
                .thenReturn(Mono.empty());

        // when
        Flux<OrderProductInfo> result = service.getBy(userId);

        // then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository).findById(userId);

        verifyNoInteractions(
                orderSearchServiceClient,
                productClient
        );
    }

    @Test
    void shouldReturnEmptyFluxWhenUserHasNoOrders() {
        // given
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.set_id(userId);
        user.setName("John Smith");
        user.setPhone("48123456789");

        when(repository.findById(userId))
                .thenReturn(Mono.just(user));

        when(orderSearchServiceClient.getOrders(user.getPhone()))
                .thenReturn(Flux.empty());

        // when
        Flux<OrderProductInfo> result = service.getBy(userId);

        // then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository).findById(userId);
        verify(orderSearchServiceClient).getOrders(user.getPhone());

        verifyNoInteractions(productClient);
    }

    @Test
    void shouldReturnOnlyOrdersForWhichProductWasFound() {
        // given
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.set_id(userId);
        user.setName("John Smith");
        user.setPhone("48123456789");

        OrderDto order1 = new OrderDto();
        order1.setOrderNumber("ORDER-1");
        order1.setProductCode("IPHONE-15");

        OrderDto order2 = new OrderDto();
        order2.setOrderNumber("ORDER-2");
        order2.setProductCode("UNKNOWN");

        ProductDto product = new ProductDto();
        product.setProductName("iPhone 15");
        product.setScore(0.95);

        when(repository.findById(userId))
                .thenReturn(Mono.just(user));

        when(orderSearchServiceClient.getOrders(user.getPhone()))
                .thenReturn(Flux.just(order1, order2));

        when(productClient.getProductWithHighestScore("IPHONE-15"))
                .thenReturn(Mono.just(product));

        when(productClient.getProductWithHighestScore("UNKNOWN"))
                .thenReturn(Mono.empty());

        // when
        Flux<OrderProductInfo> result = service.getBy(userId);

        // then
        StepVerifier.create(result)
                .assertNext(orderProductInfo -> {
                    assertEquals("ORDER-1",
                            orderProductInfo.getOrderNumber());
                    assertEquals("IPHONE-15",
                            orderProductInfo.getProductCode());
                    assertEquals("iPhone 15",
                            orderProductInfo.getProductName());
                })
                .verifyComplete();

        verify(productClient)
                .getProductWithHighestScore("IPHONE-15");

        verify(productClient)
                .getProductWithHighestScore("UNKNOWN");
    }

    @Test
    void shouldReturnMultipleOrderProductInfos() {
        // given
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.set_id(userId);
        user.setName("John Smith");
        user.setPhone("48123456789");

        OrderDto order1 = new OrderDto();
        order1.setOrderNumber("ORDER-1");
        order1.setProductCode("IPHONE-15");

        OrderDto order2 = new OrderDto();
        order2.setOrderNumber("ORDER-2");
        order2.setProductCode("MACBOOK-PRO");

        ProductDto product1 = new ProductDto();
        product1.setProductName("iPhone 15");
        product1.setScore(0.95);

        ProductDto product2 = new ProductDto();
        product2.setProductName("MacBook Pro");
        product2.setScore(0.87);

        when(repository.findById(userId))
                .thenReturn(Mono.just(user));

        when(orderSearchServiceClient.getOrders(user.getPhone()))
                .thenReturn(Flux.just(order1, order2));

        when(productClient.getProductWithHighestScore("IPHONE-15"))
                .thenReturn(Mono.just(product1));

        when(productClient.getProductWithHighestScore("MACBOOK-PRO"))
                .thenReturn(Mono.just(product2));

        // when
        Flux<OrderProductInfo> result = service.getBy(userId);

        // then
        StepVerifier.create(result)
                .assertNext(info -> {
                    assertEquals("ORDER-1", info.getOrderNumber());
                    assertEquals("IPHONE-15", info.getProductCode());
                    assertEquals("iPhone 15", info.getProductName());
                    assertEquals(
                            BigDecimal.valueOf(0.95),
                            info.getScore()
                    );
                })
                .assertNext(info -> {
                    assertEquals("ORDER-2", info.getOrderNumber());
                    assertEquals("MACBOOK-PRO", info.getProductCode());
                    assertEquals("MacBook Pro", info.getProductName());
                    assertEquals(
                            BigDecimal.valueOf(0.87),
                            info.getScore()
                    );
                })
                .verifyComplete();
    }
}
