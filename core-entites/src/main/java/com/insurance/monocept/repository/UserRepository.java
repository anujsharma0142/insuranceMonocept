package com.insurance.monocept.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	List<User> findByRole(UserRole userRole, Pageable pageable);

	@Query("select u from User u where u.user.id = :id")
	List<User> findByEmpId(Long id, Pageable pageable);

}
