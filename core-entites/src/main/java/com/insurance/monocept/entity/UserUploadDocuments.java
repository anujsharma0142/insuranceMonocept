package com.insurance.monocept.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class UserUploadDocuments {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	private String panCard;
	
	private String adhaarFront;
	
	private String adhaarBack;
	
	@OneToOne
	private User user;
	
	private boolean approved;
	
	private String status;
	
	@OneToOne
	private Insurance insurance;
}
