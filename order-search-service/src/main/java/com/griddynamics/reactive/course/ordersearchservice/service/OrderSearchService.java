package com.griddynamics.reactive.course.ordersearchservice.service;

import com.griddynamics.reactive.course.ordersearchservice.domain.Order;
import com.griddynamics.reactive.course.ordersearchservice.resource.OrderResource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OrderSearchService {

    private final OrderResource orderResource;


    public Flux<Order> getOrdersByPhone(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber))
            return Flux.error(new RuntimeException("Phone number is empty"));

        return orderResource.getOrdersByPhone(phoneNumber)
                .map(entity ->
                        new Order(entity.getPhoneNumber(), entity.getOrderNumber(), entity.getProductCode()))
                .delayElements(Duration.ofMillis(100));
    }
}
