package com.griddynamics.reactive.course.ordersearchservice.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, String> {

    Flux<OrderEntity> findAllByPhoneNumber(String phoneNumber);
}
