package com.griddynamics.reactive.course.userservice.domain;

import com.griddynamics.reactive.course.userservice.db.entity.UserEntity;
import com.griddynamics.reactive.course.userservice.integration.dto.OrderDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrichedUserInfo {
    UserEntity user;
    OrderDto order;
}
