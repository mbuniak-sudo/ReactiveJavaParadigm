package com.griddynamics.reactive.course.productinfoservice.resource;

import com.griddynamics.reactive.course.productinfoservice.db.ProductResourceRepo;
import com.griddynamics.reactive.course.productinfoservice.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductResource {

    private final ProductResourceRepo repo;


    public List<Product> getProductNamesByProductCode(String productCode) {
        return repo.findByProductCode(productCode)
                .map(productEntity -> new Product(productEntity.getId_(),
                        productEntity.getProductCode(), productEntity.getProductName(), productEntity.getScore()))
                .collectList()
                .block();
        // bad reactive style to block smth, but just to correspond with higher defined api
    }
}
