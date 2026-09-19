package com.griddynamics.reactive.course.ordersearchservice.db;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEntity {

    @Id
    private String orderNumber;

    private String phoneNumber;
    private String productCode;
}
