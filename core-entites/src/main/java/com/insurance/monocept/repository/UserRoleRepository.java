package com.insurance.monocept.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.monocept.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

	UserRole findByType(String role);

}
