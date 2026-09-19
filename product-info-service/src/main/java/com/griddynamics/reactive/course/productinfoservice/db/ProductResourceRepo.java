package com.griddynamics.reactive.course.productinfoservice.db;

import com.griddynamics.reactive.course.productinfoservice.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.List;

public interface ProductResourceRepo extends ReactiveMongoRepository<ProductEntity, Long> {

    List<Product> findByProductCode(String productCode);
}
