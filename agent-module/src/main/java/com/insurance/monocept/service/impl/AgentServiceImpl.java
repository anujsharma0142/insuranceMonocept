package com.insurance.monocept.service.impl;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.dto.AgentDto;
import com.insurance.monocept.dto.CustomerDto;
import com.insurance.monocept.dto.InsuranceDto;
import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.dto.SignUpResponseDto;
import com.insurance.monocept.dto.UserDetailsDto;
import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.entity.Insurance;
import com.insurance.monocept.entity.InsuranceScheme;
import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserDetails;
import com.insurance.monocept.entity.UserRole;
import com.insurance.monocept.entity.UserUploadDocuments;
import com.insurance.monocept.enums.UserRoles;
import com.insurance.monocept.repository.InsuranceRepository;
import com.insurance.monocept.repository.InsuranceSchemeRepository;
import com.insurance.monocept.repository.UserDetailsRepository;
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.repository.UserUploadDocumentsRepository;
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
	
	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;
	
	@Autowired
	private InsuranceRepository insuranceRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private UserUploadDocumentsRepository userUploadDocumentsRepository;
	
	@Value("${jwt.app.secret}")
	private String app_secret;
	
	
	@Override
	public ResponseEntity<?> getUserDetails(String email) {
		User user = AppUtility.getCurrentUser();
		if(user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(null);
			responseDTO.setMessage("User not found");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		}
		User customer = userRepository.findByEmail(email);
		UserDetails details = userDetailsRepository.findByUser(customer); 
		System.out.println(details);
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setData(details);
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


	@Override
	public ResponseEntity<?> getAllAddedUser(Integer pageNo) {
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
		Pageable pageable = PageRequest.of(pageNo, 10);
		List<User> users = userRepository.findByEmpId(user.getId(), pageable);
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully get user.");
		responseDTO.setStatus("success");
		responseDTO.setData(users);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<?> updateCustomer(CustomerDto customerDto) {
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
		
		User customer = userRepository.findById(customerDto.getId()).orElse(null);
		customer.setEmail(customerDto.getEmail());
		customer.setFirstName(customerDto.getFirstName());
		customer.setLastName(customerDto.getLastName());
		customer.setMobileNo(customerDto.getMobileNo());
		customer.setQualification(customerDto.getQualification());
		userRepository.save(customer);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully update customer.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<?> addInsurance(InsuranceDto insuranceDto) {
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
		User customer = userRepository.findByEmail(insuranceDto.getEmail());
		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(insuranceDto.getInsuranceSchemeId()).orElse(null);
		
		Insurance insurance = insuranceRepository.findByUserAndConpleted(customer.getId());
		if(insurance != null) {
			insuranceRepository.delete(insurance);
		}
		insurance = new Insurance();
		insurance.setInsuranceScheme(insuranceScheme);
		insurance.setUser(customer);
		insurance.setCompleted(false);
		insurance.setStatus("ONGOING");
		insurance.setCompletedSteps("2");
		insurance.setActive(false);
		insuranceRepository.save(insurance);
		insurance.setAccNo("ACCMONOINS2023"+insurance.getId());
		insuranceRepository.save(insurance);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setData(insurance.getId());		
		responseDTO.setMessage("Successfully add insurance.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> uploadDocuments(MultipartFile panCard, MultipartFile adhaarFront,
			MultipartFile adhaarBack, long insuranceId) {
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
		System.out.println(panCard.getOriginalFilename());
		System.out.println(adhaarFront.getOriginalFilename());
		System.out.println(adhaarBack.getOriginalFilename());
		System.out.println();
		Insurance insurance = insuranceRepository.findById(insuranceId).orElse(null);
		UserUploadDocuments documents = userUploadDocumentsRepository.findByUserAndInsurance(insurance.getUser(),insurance);
		if(documents == null) {
			documents = new UserUploadDocuments();
		
				try {
					String fileName = StringUtils.cleanPath(panCard.getOriginalFilename());
					byte[] bytes = panCard.getBytes();
					Path path = Paths.get("./src/main/resources/templates/" + fileName);
			        Files.write(path, bytes);
			        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
			        documents.setPanCard(imagePath);			      
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				}
				try {
					String fileName = StringUtils.cleanPath(adhaarFront.getOriginalFilename());
					byte[] bytes = adhaarFront.getBytes();
					Path path = Paths.get("./src/main/resources/templates/" + fileName);
			        Files.write(path, bytes);
			        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
			        documents.setAdhaarFront(imagePath);			      
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				}
				try {
					String fileName = StringUtils.cleanPath(adhaarBack.getOriginalFilename());
					byte[] bytes = adhaarBack.getBytes();
					Path path = Paths.get("./src/main/resources/templates/" + fileName);
			        Files.write(path, bytes);
			        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
			        documents.setAdhaarBack(imagePath);			      
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				}
				documents.setInsurance(insurance);
				documents.setUser(insurance.getUser());
				documents.setStatus("PENDING");
				userUploadDocumentsRepository.save(documents);	
				insurance.setCompletedSteps("3");
				insuranceRepository.save(insurance);
		}
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully add documents");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addUserDetails(UserDetailsDto userDetailsDto) {
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
		User customer = userRepository.findByEmail(userDetailsDto.getEmail());
		UserDetails userDetails = userDetailsRepository.findByUser(customer);
		if(userDetails != null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("successfully add user Details");
			responseDTO.setStatus("success");
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}
		userDetails = new UserDetails();
		userDetails.setAddress(userDetailsDto.getAddress());
		userDetails.setCity(userDetailsDto.getCity());
		userDetails.setPincode(userDetailsDto.getPincode());
		userDetails.setState(userDetailsDto.getState());
		userDetails.setPanCard(userDetailsDto.getPanCard());
		userDetails.setNomineeName(userDetailsDto.getNomineeName());
		userDetails.setNomineeNo(userDetailsDto.getNomineeNo());
		userDetails.setNomineeRelation(userDetailsDto.getNomineeRelation());
		userDetails.setUser(customer);
		userDetailsRepository.save(userDetails);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully add user Details");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> getAllInsuranceScheme() {
		User user = AppUtility.getCurrentUser();
		
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_AGENT")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Agent credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		List<InsuranceScheme> insuranceSchemes = insuranceSchemeRepository.findAll();
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully get insurance schemes");
		responseDTO.setStatus("success");
		responseDTO.setData(insuranceSchemes);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<?> getInsuranceByUser(Long userId) {
		User user = AppUtility.getCurrentUser();
		
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_AGENT")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Agent credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
			
			
			User customer = userRepository.findById(userId).orElseGet(null);
			
			Insurance insurance = insuranceRepository.findByUserAndConpleted(customer.getId());
			
			ResponseDto responseDTO = new ResponseDto();
			
			responseDTO.setMessage("successfully get insurance schemes");
			responseDTO.setStatus("success");
			responseDTO.setData(insurance);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<?> getInsuranceByUser(String email) {
		User user = AppUtility.getCurrentUser();
		
		
		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		if(!user.getRole().getType().equals("ROLE_AGENT")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Agent credentials invalid.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
			
			
			User customer = userRepository.findByEmail(email);
			
			Insurance insurance = insuranceRepository.findByUserAndConpleted(customer.getId());
			
			ResponseDto responseDTO = new ResponseDto();
			
			responseDTO.setMessage("successfully get insurance schemes");
			responseDTO.setStatus("success");
			responseDTO.setData(insurance);
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	
}
