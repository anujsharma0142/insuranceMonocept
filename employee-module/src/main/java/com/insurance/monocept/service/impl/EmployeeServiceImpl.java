package com.insurance.monocept.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.service.EmployeeService;
import com.insurance.monocept.utility.AppUtility;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
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
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_EMPLOYEE")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		User agent = userRepository.findByEmail(agentDto.getEmail());
		
		if(agent != null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("agent email Id already exist.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		UserRole userRole = userRoleRepository.findByType(UserRoles.AGENT.getRole());
		
		agent = new User();
		agent.setEmail(agentDto.getEmail());
		agent.setEmailVerified(true);
		agent.setFirstName(agentDto.getFirstName());
		agent.setIsLoginAllowed(true);
		agent.setLastName(agentDto.getLasttName());
		agent.setMobileNo(agentDto.getPhoneNo());
		agent.setPassword(encoder.encode(agentDto.getPassword()));
		agent.setRole(userRole);
		agent.setQualification(agentDto.getQalification());
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
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_EMPLOYEE")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
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
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_EMPLOYEE")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		User employee = userRepository.findByEmail(employeeDto.getEmail());
		
		if(employee == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		
		employee.setEmail(employeeDto.getEmail());
		employee.setFirstName(employeeDto.getFirstName());
		employee.setIsLoginAllowed(employeeDto.isLoginAllowed());
		employee.setLastName(employeeDto.getLasttName());
		employee.setMobileNo(employeeDto.getPhoneNo());
		employee.setPassword(encoder.encode(employeeDto.getPassword()));
		employee.setQualification(employeeDto.getQalification());
		userRepository.save(employee);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully update agent.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

}
