package com.insurance.monocept.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.monocept.entity.Insurance;

public interface InsuranceRepository  extends JpaRepository<Insurance, Long>{

	@Query("select c from Insurance c where c.completed = false and c.user.id = :id")
	Insurance findByUserAndConpleted(Long id);

}
