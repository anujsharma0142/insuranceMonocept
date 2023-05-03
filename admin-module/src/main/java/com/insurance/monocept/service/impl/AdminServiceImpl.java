package com.insurance.monocept.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.insurance.monocept.dto.AdminDto;
import com.insurance.monocept.dto.EmployeeDto;
import com.insurance.monocept.dto.InsuranceTypeDto;
import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.dto.SignUpResponseDto;
import com.insurance.monocept.entity.InsuranceType;
import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;
import com.insurance.monocept.enums.UserRoles;
import com.insurance.monocept.repository.InsuranceTypeRepository;
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.service.AdminService;
import com.insurance.monocept.utility.AppUtility;
import com.insurance.monocept.utility.TokenUtility;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Value("${jwt.app.secret}")
	private String app_secret;
	
	@Autowired
	private InsuranceTypeRepository insuranceTypeRepository;
	
	 private final Path root = Paths.get("./src/main/resources/templates/");
	
	@Override
	public ResponseEntity<?> addEmployee(EmployeeDto employeeDto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		User employee = userRepository.findByEmail(employeeDto.getEmail());
		
		if(employee != null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee email Id already exist.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		UserRole userRole = userRoleRepository.findByType(UserRoles.EMPLOYEE.getRole());
		
		employee = new User();
		employee.setEmail(employeeDto.getEmail());
		employee.setEmailVerified(true);
		employee.setFirstName(employeeDto.getFirstName());
		employee.setLastName(employeeDto.getLasttName());
		employee.setMobileNo(employeeDto.getPhoneNo());
		employee.setPassword(encoder.encode(employeeDto.getPassword()));
		employee.setLoginAllowed(true);
		employee.setRole(userRole);
		employee.setQualification(employeeDto.getQalification());
		userRepository.save(employee);
		employee.setLoginId("MONOINS" + employee.getId());
		userRepository.save(employee);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully save employee.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity<?> addAgent(EmployeeDto agentDto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
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
		agent.setLoginAllowed(true);
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
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
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
	public ResponseEntity<?> getEmployee(Integer pageNo) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		UserRole userRole = userRoleRepository.findByType(UserRoles.EMPLOYEE.getRole());
		Pageable pageable = PageRequest.of(pageNo, 20);
		List<User> employee = userRepository.findByRole(userRole, pageable);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully get employee.");
		responseDTO.setStatus("success");
		responseDTO.setData(employee);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateEmployee(EmployeeDto employeeDto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
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
		responseDTO.setMessage("Successfully update employee.");
		responseDTO.setStatus("success");
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
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
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

	@Override
	public ResponseEntity<?> addAdmin(AdminDto adminDto) {
		User user = userRepository.findByEmail(adminDto.getEmail());

		if (user != null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Email already exists please Login");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		} else {
			String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(adminDto.getEmail());
			if (!matcher.matches()) {
				ResponseDto responseDTO = new ResponseDto();
				responseDTO.setMessage("Email is not valid.");
				responseDTO.setStatus("fail");
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}

			UserRole userRole = userRoleRepository.findByType(UserRoles.ADMIN.getRole());
			if (userRole == null) {
				userRole = new UserRole();
				userRole.setType("ROLE_ADMIN");
				userRoleRepository.save(userRole);
			}
			user = new User();
			user.setEmail(adminDto.getEmail());
			user.setFirstName(adminDto.getFirstName());
			user.setLastName(adminDto.getLastName());
			user.setMobileNo(adminDto.getPhoneNo());
			user.setQualification(adminDto.getQualification());
			user.setEmailVerified(true);
			user.setLoginAllowed(true);
			user.setPassword(encoder.encode(adminDto.getPassword()));
			user.setRole(userRole);
			user.setLastLoginDate(String.valueOf(LocalDate.now()));
			user.setLastLoginTime(String.valueOf(LocalTime.now()));
			userRepository.save(user);

			String jwtToken = TokenUtility.createJWT(user, app_secret, "apiToken", new ArrayList<>());
			SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
			signUpResponseDto.setEmail(adminDto.getEmail());
			signUpResponseDto.setToken(jwtToken);

			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(signUpResponseDto);
			responseDTO.setMessage("Admin signup successfully");
			responseDTO.setStatus("success");
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> addInsuranceType(InsuranceTypeDto insurancedto) {
		User user = AppUtility.getCurrentUser();
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		InsuranceType insurancetype = new InsuranceType();
		
		try {
			String fileName = StringUtils.cleanPath(insurancedto.getImage().getOriginalFilename());
			byte[] bytes = insurancedto.getImage().getBytes();
			Path path = Paths.get("./src/main/resources/templates/" + fileName);
	        Files.write(path, bytes);
	        String imagePath = "http://localhost:8083/api/vi/admin/getImage/" +  fileName;
	        insurancetype.setImgPath(imagePath);
	      
		} catch (IllegalStateException | IOException  e) {
			e.printStackTrace();
		}
		
		insurancetype.setActive(true);
		insurancetype.setName(insurancedto.getName());
		insuranceTypeRepository.save(insurancetype);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully insurance type");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
	
	@Override
	  public ResponseEntity<byte[]> getImage(String filename) {
	    	 byte[] image = new byte[0];
	         try {
	             image = FileUtils.readFileToByteArray(new File("./src/main/resources/templates/" + filename));
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	         return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	         
//	         
//	      Path file = root.resolve(filename);
//	      Resource resource = new UrlResource(file.toUri());
//
//	      if (resource.exists() || resource.isReadable()) {
//	        return resource;
//	      } else {
//	        throw new RuntimeException("Could not read the file!");
//	      }
//	    } catch (MalformedURLException e) {
//	      throw new RuntimeException("Error: " + e.getMessage());
//	    }
	  }

}
