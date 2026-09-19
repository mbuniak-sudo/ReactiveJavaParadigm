package com.griddynamics.reactive.course.userservice.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProductInfo {
    String userName;
    String phoneNumber;
    String orderNumber;
    String productCode;
    String productName;
    BigDecimal score;
}
