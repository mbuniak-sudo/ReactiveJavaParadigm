package com.griddynamics.reactive.course.productinfoservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductEntity {

    Long id_;
    String productCode;
    String productName;
    Double score;
}
