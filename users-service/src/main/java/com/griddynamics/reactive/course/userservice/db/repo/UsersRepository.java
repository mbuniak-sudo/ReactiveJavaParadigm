package com.griddynamics.reactive.course.userservice.db.repo;

import com.griddynamics.reactive.course.userservice.db.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UsersRepository extends ReactiveMongoRepository<UserEntity, Long> {

}
