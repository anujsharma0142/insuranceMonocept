package com.insurance.monocept.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.insurance.monocept.entity.PremiumPaymentDetails;

public interface PremiumPaymentDetailsRepository extends JpaRepository<PremiumPaymentDetails, Long>{
	
	@Query("select p from PremiumPaymentDetails p where p.paid = :status and p.insurance.user.id=:id")
	List<PremiumPaymentDetails> findByStatus(Boolean status, Long id);

}
