package com.griddynamics.reactive.course.userservice.service;

import com.griddynamics.reactive.course.userservice.domain.OrderProductInfo;
import reactor.core.publisher.Flux;

public interface UserService {

    Flux<OrderProductInfo> getBy(Long id);
}
