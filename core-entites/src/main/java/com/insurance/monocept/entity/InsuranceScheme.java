package com.insurance.monocept.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class InsuranceScheme {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	private String name;
	
	private float commisionForInstallment;
	
	private float commisionForRegistration;
	
	private String img;
	
	private double assuredAmount;
	
	private String status;
	
	@ManyToOne
	private InsuranceType insuranceType; 
	
	private int duration;
	
	private int insuranceTax;
	
	private Long profitRatio;
	
	

}
