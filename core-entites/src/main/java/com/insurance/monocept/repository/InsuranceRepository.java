package com.insurance.monocept.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.monocept.entity.Insurance;
import com.insurance.monocept.entity.User;

public interface InsuranceRepository  extends JpaRepository<Insurance, Long>{

	@Query("select c from Insurance c where c.completed = false and c.user.id = :id")
	Insurance findByUserAndConpleted(Long id);
	
	@Query("select c from Insurance c where (c.completed = false and c.user.id = :id) and c.insuranceScheme.id = :insuranceId")
	Insurance findByUserAndConpleted(Long id, long insuranceId);

	List<Insurance> findByUser(User user);

}
