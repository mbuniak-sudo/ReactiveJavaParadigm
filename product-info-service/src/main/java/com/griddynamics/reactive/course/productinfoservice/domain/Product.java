package com.griddynamics.reactive.course.productinfoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private final Long productId;
    private final String productCode;
    private final String productName;
    private final double score;
}
