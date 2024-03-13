package com.service.userService.repository;

import com.service.userService.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Long> {


}
