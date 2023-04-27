package com.insurance.monocept.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.monocept.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

}
