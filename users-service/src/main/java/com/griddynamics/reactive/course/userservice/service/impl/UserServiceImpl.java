package com.griddynamics.reactive.course.userservice.service.impl;

import com.griddynamics.reactive.course.userservice.db.repo.UsersRepository;
import com.griddynamics.reactive.course.userservice.domain.OrderProductInfo;
import com.griddynamics.reactive.course.userservice.domain.EnrichedUserInfo;
import com.griddynamics.reactive.course.userservice.integration.OrderSearchServiceClient;
import com.griddynamics.reactive.course.userservice.integration.ProductInfoSearchServiceClient;
import com.griddynamics.reactive.course.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository repository;
    private final ProductInfoSearchServiceClient productClient;
    private final OrderSearchServiceClient orderSearchServiceClient;


    @Override
    public Flux<OrderProductInfo> getBy(Long id) {
        return repository.findById(id)
                .flatMapMany(userEntity -> orderSearchServiceClient.getOrders(userEntity.getPhone())
                        .map(orderDto -> new EnrichedUserInfo(userEntity, orderDto)))
                .flatMap(enrichedOrder ->
                        productClient.getProductWithHighestScore(enrichedOrder.getOrder().getProductCode())
                                .map(productDto -> OrderProductInfo.builder()
                                        .userName(enrichedOrder.getUser().getName())
                                        .phoneNumber(enrichedOrder.getUser().getPhone())
                                        .orderNumber(enrichedOrder.getOrder().getOrderNumber())
                                        .productCode(enrichedOrder.getOrder().getProductCode())
                                        .productName(productDto.getProductName())
                                        .score(BigDecimal.valueOf(productDto.getScore()))
                                        .build()));
    }
}
