package com.griddynamics.reactive.course.userservice.controller;

import com.griddynamics.reactive.course.userservice.domain.OrderProductInfo;
import com.griddynamics.reactive.course.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
@RequestMapping("/userService")
public class UserController {

    private final UserService service;


    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<OrderProductInfo> getUsers(@PathVariable Long userId) {
        return service.getBy(userId);
    }
}
