package com.insurance.monocept.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.monocept.entity.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>{

}
