package com.insurance.monocept.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insurance.monocept.dto.EmployeeDto;
import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;
import com.insurance.monocept.enums.UserRoles;
import com.insurance.monocept.exception.AgentEmailIdAlreadyExists;
import com.insurance.monocept.exception.EmailNotValidException;
import com.insurance.monocept.exception.UserNotFoundException;
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.service.EmployeeService;
import com.insurance.monocept.utility.AppUtility;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	public static final Logger LOGGER=LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	
	@Override
	public ResponseEntity<?> addAgent(EmployeeDto agentDto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			LOGGER.error("User not found");
			throw new UserNotFoundException("User not Found");
		}
		if(!user.getRole().getType().equals("ROLE_EMPLOYEE")) {
			LOGGER.error("Invalid credentials");
			throw new EmailNotValidException("Employee credentials invalid");
		}
		
		User agent = userRepository.findByEmail(agentDto.getEmail());
		
		if(agent != null) {
			LOGGER.error("Agent email Id already exists");
			throw new AgentEmailIdAlreadyExists("agent email Id already exist");
		}
		
		UserRole userRole = userRoleRepository.findByType(UserRoles.AGENT.getRole());
		
		agent = new User();
		agent.setEmail(agentDto.getEmail());
		agent = new User();
		agent.setEmail(agentDto.getEmail());
		agent.setEmailVerified(true);
		agent.setFirstName(agentDto.getFirstName());
		agent.setLoginAllowed(true);
		agent.setLastName(agentDto.getLastName());
		agent.setMobileNo(agentDto.getMobileNo());
		agent.setPassword(encoder.encode(agentDto.getPassword()));
		agent.setRole(userRole);
		agent.setQualification(agentDto.getQualification());
		userRepository.save(agent);
		agent.setLoginId("MONOINS" + agent.getId());
		userRepository.save(agent);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully save agent.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAgent(Integer pageNo) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			LOGGER.error("User not found");
			throw new UserNotFoundException("User not Found");
		}
		if(!user.getRole().getType().equals("ROLE_EMPLOYEE")) {
			LOGGER.error("Invalid credentials");
			throw new EmailNotValidException("Employee credentials invalid");
		}
		UserRole userRole = userRoleRepository.findByType(UserRoles.AGENT.getRole());
		Pageable pageable = PageRequest.of(pageNo, 20);
		List<User> agents = userRepository.findByRole(userRole, pageable);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully get agents.");
		responseDTO.setStatus("success");
		responseDTO.setData(agents);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	
	@Override
	public ResponseEntity<?> updateAgent(EmployeeDto employeeDto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			LOGGER.error("User not found");
			throw new UserNotFoundException("User not Found");
		}
		if(!user.getRole().getType().equals("ROLE_EMPLOYEE")) {
			LOGGER.error("Invalid credentials");
			throw new EmailNotValidException("Employee credentials invalid");
		}
		
		User employee = userRepository.findByEmail(employeeDto.getEmail());
		
		if(employee == null) {
			LOGGER.error("Employee not found");
			throw new UserNotFoundException("Employee not Found");
		}
		
		
		employee.setEmail(employeeDto.getEmail());
		employee.setFirstName(employeeDto.getFirstName());
		employee.setIsLoginAllowed(employeeDto.isLoginAllowed());
		employee.setLastName(employeeDto.getLastName());
		employee.setMobileNo(employeeDto.getMobileNo());
		employee.setPassword(encoder.encode(employeeDto.getPassword()));
		employee.setQualification(employeeDto.getQualification());
		userRepository.save(employee);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully update agent.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

}
