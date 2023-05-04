package com.insurance.monocept.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.monocept.entity.Insurance;

public interface InsuranceRepository  extends JpaRepository<Insurance, Long>{

}
