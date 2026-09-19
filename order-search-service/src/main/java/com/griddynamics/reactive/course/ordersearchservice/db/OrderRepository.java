package com.griddynamics.reactive.course.ordersearchservice.db;

import com.griddynamics.reactive.course.ordersearchservice.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.List;

public interface OrderRepository extends ReactiveMongoRepository<OrderEntity, String> {

    List<Order> findAllByPhoneNumber(String phoneNumber);
}
