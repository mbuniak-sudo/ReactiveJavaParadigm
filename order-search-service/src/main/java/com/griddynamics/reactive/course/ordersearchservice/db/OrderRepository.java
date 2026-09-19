package com.griddynamics.reactive.course.ordersearchservice.db;

import com.griddynamics.reactive.course.ordersearchservice.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, String> {

    Flux<Order> findAllByPhoneNumber(String phoneNumber);
}
