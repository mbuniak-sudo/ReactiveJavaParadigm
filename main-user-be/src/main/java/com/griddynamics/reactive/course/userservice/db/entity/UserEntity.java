package com.griddynamics.reactive.course.userservice.db.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    Long _id;
    String name;
    String phone;
}
