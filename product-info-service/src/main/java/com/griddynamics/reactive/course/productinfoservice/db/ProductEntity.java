package com.griddynamics.reactive.course.productinfoservice.db;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductEntity {

    @Id
    Long id_;
    String productCode;
    String productName;
    Double score;
}
