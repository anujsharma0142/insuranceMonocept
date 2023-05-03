package com.insurance.monocept.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class InsuranceTypeDto {
	
	
	private Long id;
	
	private String imgPath;
	
	private boolean active;
	
	private String name;
	
	private MultipartFile image;


}
