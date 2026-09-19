package com.griddynamics.reactive.course.ordersearchservice.resource;

import com.griddynamics.reactive.course.ordersearchservice.db.OrderRepository;
import com.griddynamics.reactive.course.ordersearchservice.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderResource {

    private final OrderRepository repository;


    public List<Order> getOrdersByPhone(String phoneNumber) {
        return repository.findAllByPhoneNumber(phoneNumber);
    }
}
