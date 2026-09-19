package com.griddynamics.reactive.course.userservice.controller;

import com.griddynamics.reactive.course.userservice.domain.OrderProductInfo;
import com.griddynamics.reactive.course.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;


    @Test
    void shouldReturnUsers() {
        Long userId = 123L;

        OrderProductInfo first = new OrderProductInfo();
        OrderProductInfo second = new OrderProductInfo();

        when(service.getBy(userId))
                .thenReturn(Flux.just(first, second));

        Flux<OrderProductInfo> result = controller.getUsers(userId);

        StepVerifier.create(result)
                .expectNext(first)
                .expectNext(second)
                .verifyComplete();

        verify(service).getBy(userId);
    }
}
