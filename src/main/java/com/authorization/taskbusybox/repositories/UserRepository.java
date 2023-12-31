package com.authorization.taskbusybox.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authorization.taskbusybox.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> findByEmail(String email);

}
