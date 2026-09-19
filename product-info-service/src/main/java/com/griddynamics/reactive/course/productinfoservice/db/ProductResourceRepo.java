package com.griddynamics.reactive.course.productinfoservice.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductResourceRepo extends ReactiveMongoRepository<ProductEntity, Long> {

    Flux<ProductEntity> findByProductCode(String productCode);
}
