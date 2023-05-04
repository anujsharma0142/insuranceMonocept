package com.insurance.monocept.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insurance.monocept.dto.AgentDto;
import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.dto.SignUpResponseDto;
import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;
import com.insurance.monocept.enums.UserRoles;
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.service.AgentService;
import com.insurance.monocept.utility.AppUtility;
import com.insurance.monocept.utility.TokenUtility;

@Service
public class AgentServiceImpl implements AgentService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${jwt.app.secret}")
	private String app_secret;
	
	
	@Override
	public ResponseEntity<?> getUserDetails() {
		User user = AppUtility.getCurrentUser();
		if(user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(null);
			responseDTO.setMessage("User not found");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		}
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setData(user);
		responseDTO.setMessage("get user details successfully");
		responseDTO.setStatus("Success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<?> addCustomer(AgentDto agentDto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_AGENT")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		User user1 = userRepository.findByEmail(agentDto.getEmail());
		
		if(user1 != null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("user email Id already exist.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		UserRole userRole = userRoleRepository.findByType(UserRoles.USER.getRole());
		
		user1 = new User();
		user1.setEmail(agentDto.getEmail());
		user1.setEmailVerified(true);
		user1.setFirstName(agentDto.getFirstName());
		user1.setLoginAllowed(true);
		user1.setLastName(agentDto.getLastName());
		user1.setMobileNo(agentDto.getPhoneNo());
		user1.setPassword(encoder.encode(agentDto.getPassword()));
		user1.setRole(userRole);
		user1.setUser(user);
		user1.setQualification(agentDto.getQualification());
		userRepository.save(user1);
		user1.setLoginId("MONOINS" + user1.getId());
		userRepository.save(user1);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully save user.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	
}
