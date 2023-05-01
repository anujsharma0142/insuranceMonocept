package com.insurance.monocept.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	List<User> findByRole(UserRole userRole, Pageable pageable);

}
