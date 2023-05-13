package com.insurance.monocept.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class UserDetails {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	private String address;
	
	private String pincode;
	
	private String city;
	
	private String state;
	
	private String panCard;
	
	private String nomineeNo;
	
	private String nomineeRelation;
	
	private String nomineeName;
	
	private String dob;
	
	@ManyToOne
	private User user;
	
}
