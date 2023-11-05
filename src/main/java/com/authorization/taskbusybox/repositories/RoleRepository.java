package com.authorization.taskbusybox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authorization.taskbusybox.entities.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer>{

	RoleEntity findRoleByName(String name);
}
