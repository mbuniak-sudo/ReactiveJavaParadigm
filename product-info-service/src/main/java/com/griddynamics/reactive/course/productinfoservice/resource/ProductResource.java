package com.griddynamics.reactive.course.productinfoservice.resource;

import com.griddynamics.reactive.course.productinfoservice.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductResource {

    private static final List<Product> entities = List.of(
            new Product(0L, "101", "Air Grill Jacobs", 100.99),
            new Product(1L, "102", "Smart Coffee Maker", 79.99),
            new Product(2L, "103", "Wireless Headphones", 149.50),
            new Product(3L, "104", "Electric Kettle", 45.99),
            new Product(4L, "105", "Robot Vacuum Cleaner", 299.99),
            new Product(5L, "106", "Digital Kitchen Scale", 29.95),
            new Product(6L, "107", "Bluetooth Speaker", 89.90),
            new Product(7L, "108", "Stainless Steel Blender", 119.99));


    public List<Product> getProductNamesByProductCode(String productCode) {
        return entities.stream()
                .filter(entity -> entity.getProductCode().equalsIgnoreCase(productCode))
                .collect(Collectors.toList());
    }
}
