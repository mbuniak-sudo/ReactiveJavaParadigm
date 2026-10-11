package com.griddynamics.reactive.course.ordersearchservice.resource;

import com.griddynamics.reactive.course.ordersearchservice.db.OrderEntity;
import com.griddynamics.reactive.course.ordersearchservice.db.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class OrderResource {

    private final OrderRepository repository;


    public Flux<OrderEntity> getOrdersByPhone(String phoneNumber) {
        return repository.findAllByPhoneNumber(phoneNumber);
    }
}
