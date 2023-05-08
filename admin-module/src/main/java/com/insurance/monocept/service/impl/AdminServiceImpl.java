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
import com.insurance.monocept.dto.InsuranceSchemeDto;
import com.insurance.monocept.dto.InsuranceTypeDto;
import com.insurance.monocept.dto.PremiumPaymentDetailsDto;
import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.dto.SignUpResponseDto;
import com.insurance.monocept.dto.UserUplaodDocumentsDto;
import com.insurance.monocept.entity.InsuranceScheme;
import com.insurance.monocept.entity.InsuranceType;
import com.insurance.monocept.entity.PremiumPaymentDetails;
import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;
import com.insurance.monocept.entity.UserUploadDocuments;
import com.insurance.monocept.enums.UserRoles;
import com.insurance.monocept.repository.InsuranceRepository;
import com.insurance.monocept.repository.InsuranceSchemeRepository;
import com.insurance.monocept.repository.InsuranceTypeRepository;
import com.insurance.monocept.repository.PremiumPaymentDetailsRepository;
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.repository.UserUploadDocumentsRepository;
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
	
	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;
	
	@Autowired
	private PremiumPaymentDetailsRepository premiumPaymentDetailsRepository;
	
	@Autowired
	private UserUploadDocumentsRepository userUploadDocumentsRepository;
	
	@Autowired
	private InsuranceRepository insuranceRepository;
	
	@Autowired
	private PremiumPaymentDetailsRepository paymentDetailsRepository;
	
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
		employee.setLastName(employeeDto.getLastName());
		employee.setMobileNo(employeeDto.getMobileNo());
		employee.setPassword(encoder.encode(employeeDto.getPassword()));
		employee.setLoginAllowed(true);
		employee.setRole(userRole);
		employee.setQualification(employeeDto.getQualification());
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
		System.out.println(agentDto.toString());
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
		Pageable pageable = PageRequest.of(pageNo, 10);
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
		Pageable pageable = PageRequest.of(pageNo, 10);
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
		
		User employee = userRepository.findById(employeeDto.getId()).orElse(null);
		
		if(employee == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		
		employee.setEmail(employeeDto.getEmail());
		employee.setFirstName(employeeDto.getFirstName());
		employee.setIsLoginAllowed(employeeDto.isLoginAllowed());
		employee.setLastName(employeeDto.getLastName());
		employee.setMobileNo(employeeDto.getMobileNo());
		employee.setPassword(encoder.encode(employeeDto.getPassword()));
		employee.setQualification(employeeDto.getQualification());
		employee.setLoginAllowed(employeeDto.isLoginAllowed());		
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
		
		User employee = userRepository.findById(employeeDto.getId()).orElse(null);
		
		if(employee == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Employee not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		
		employee.setEmail(employeeDto.getEmail());
		employee.setFirstName(employeeDto.getFirstName());
		employee.setIsLoginAllowed(employeeDto.isLoginAllowed());
		employee.setLastName(employeeDto.getLastName());
		employee.setMobileNo(employeeDto.getMobileNo());
		employee.setPassword(encoder.encode(employeeDto.getPassword()));
		employee.setQualification(employeeDto.getQualification());
		employee.setLoginAllowed(employeeDto.isLoginAllowed());
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
	        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
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

	@Override
	public ResponseEntity<?> addInsuranceScheme(InsuranceSchemeDto insuranceSchemedto) {
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
		
		
		InsuranceScheme insuranceScheme = new InsuranceScheme();
		
		
		InsuranceType insuranceType = insuranceTypeRepository.findById(insuranceSchemedto.getInsuranceType()).orElse(null);
		insuranceScheme.setCommisionForInstallment(insuranceSchemedto.getCommissionForInstallment());
		insuranceScheme.setCommisionForRegistration(insuranceSchemedto.getCommissionForRegistration());
		insuranceScheme.setDuration(insuranceSchemedto.getDuration());
		insuranceScheme.setInsuranceTax(insuranceSchemedto.getInsuranceTax());
		insuranceScheme.setProfitRatio(insuranceSchemedto.getProfitRatio());
		insuranceScheme.setAssuredAmount(insuranceSchemedto.getAssuredAmount());
		insuranceScheme.setName(insuranceSchemedto.getName());
		insuranceScheme.setStatus("active");
		try {
			String fileName = StringUtils.cleanPath(insuranceSchemedto.getImg().getOriginalFilename());
			byte[] bytes = insuranceSchemedto.getImg().getBytes();
			Path path = Paths.get("./src/main/resources/templates/" + fileName);
	        Files.write(path, bytes);
	        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
	        insuranceScheme.setImg(imagePath);
	      
		} catch (IllegalStateException | IOException  e) {
			e.printStackTrace();
		}
		
		
		insuranceScheme.setInsuranceType(insuranceType);
		
		insuranceSchemeRepository.save(insuranceScheme);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully insurance scheme");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		
		
		
	}

	@Override
	public ResponseEntity<?> premiumPaymentDetails(PremiumPaymentDetailsDto premiumPaymentDetailsdto) {
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
		
		PremiumPaymentDetails premiumPaymentDetails = new PremiumPaymentDetails();
		
		premiumPaymentDetails.setAmount(premiumPaymentDetailsdto.getAmount());
		premiumPaymentDetails.setPaymentDetails(premiumPaymentDetailsdto.getPaymentDetails());
		premiumPaymentDetails.setStatus(premiumPaymentDetailsdto.getStatus());
		premiumPaymentDetails.setType(premiumPaymentDetailsdto.getType());
		
		premiumPaymentDetailsRepository.save(premiumPaymentDetails);	
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully save paymet details");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> approvedDocuments(boolean approved, long documentId) {
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
		UserUploadDocuments documents = userUploadDocumentsRepository.findById(documentId).orElse(null);
		if(approved) {
			documents.setStatus("APPROVED");
			documents.getInsurance().setCompletedSteps("4");
			documents.getInsurance().setActive(false);
			insuranceRepository.save(documents.getInsurance());
			PremiumPaymentDetails details = new PremiumPaymentDetails();
			Double amount = (documents.getInsurance().getInsuranceScheme().getAssuredAmount() *10)/100;
			details.setAmount(amount);	
			details.setInsurance(documents.getInsurance());
			details.setPaid(false);
			details.setPaymentDetails("initial payment");
			details.setStatus("pending");
			details.setType("insurance_type");
			paymentDetailsRepository.save(details);
		}
		else documents.setStatus("REJECTED");
		documents.setApproved(approved);
		userUploadDocumentsRepository.save(documents);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully change status of documents");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getUserDocuments() {
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
		List<UserUploadDocuments> documents = userUploadDocumentsRepository.findByStatus("PENDING");
		List<UserUplaodDocumentsDto> documentsDtos = new ArrayList<>();
		for(UserUploadDocuments doc : documents) {
			UserUplaodDocumentsDto dto = new UserUplaodDocumentsDto();
			dto.setAdhaarBack(doc.getAdhaarBack());
			dto.setAdhaarFront(doc.getAdhaarFront());
			dto.setApproved(doc.isApproved());
			dto.setId(doc.getId());
			dto.setPanCard(doc.getPanCard());
			dto.setStatus(doc.getStatus());
			documentsDtos.add(dto);
		}
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully get documents");
		responseDTO.setStatus("success");
		responseDTO.setData(documentsDtos);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> changePassword(String password, long id) {
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
		User userChangePassword = userRepository.findById(id).orElse(null);
		if(userChangePassword ==  null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		}
		userChangePassword.setPassword(encoder.encode(password));
		userRepository.save(userChangePassword);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully change password");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getInsuranceType() {
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
		
		List<InsuranceType> list = insuranceTypeRepository.findAll();
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setData(list);
		responseDTO.setMessage("successfully get insurance type");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity<?> updateInsuranceType(InsuranceTypeDto insuranceTypeDto) {
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
		System.out.println(insuranceTypeDto.toString());
		InsuranceType  insuranceType = insuranceTypeRepository.findById(insuranceTypeDto.getId()).orElse(null);
		
		insuranceType.setName(insuranceTypeDto.getName());
		
		if(insuranceTypeDto.getImage()!= null) {
			try {
				String fileName = StringUtils.cleanPath(insuranceTypeDto.getImage().getOriginalFilename());
				byte[] bytes = insuranceTypeDto.getImage().getBytes();
				Path path = Paths.get("./src/main/resources/templates/" + fileName);
		        Files.write(path, bytes);
		        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
		        insuranceType.setImgPath(imagePath);
		      
			} catch (IllegalStateException | IOException  e) {
				e.printStackTrace();
			}
			
		}
		insuranceType.setActive(insuranceTypeDto.isActive());
		insuranceTypeRepository.save(insuranceType);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully update insurance type");
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
		if(!user.getRole().getType().equals("ROLE_ADMIN")) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Admin credentials invalid.");
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
	public ResponseEntity<?> updateInsuranceScheme(InsuranceSchemeDto insuranceSchemeDto) {
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
		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(insuranceSchemeDto.getId()).orElse(null);
		System.out.println(insuranceSchemeDto.getInsuranceType());
		if(insuranceSchemeDto.getInsuranceType() != null && !insuranceSchemeDto.getInsuranceType().equals("")) {
			InsuranceType insuranceType = insuranceTypeRepository.findById(insuranceSchemeDto.getInsuranceType()).orElse(null);
			insuranceScheme.setInsuranceType(insuranceType);
		}
		insuranceScheme.setCommisionForInstallment(insuranceSchemeDto.getCommissionForInstallment());
		insuranceScheme.setCommisionForRegistration(insuranceSchemeDto.getCommissionForRegistration());
		insuranceScheme.setDuration(insuranceSchemeDto.getDuration());
		insuranceScheme.setInsuranceTax(insuranceSchemeDto.getInsuranceTax());
		insuranceScheme.setProfitRatio(insuranceSchemeDto.getProfitRatio());
		insuranceScheme.setAssuredAmount(insuranceSchemeDto.getAssuredAmount());
		insuranceScheme.setName(insuranceSchemeDto.getName());
		insuranceScheme.setStatus(insuranceSchemeDto.getStatus());
		if(insuranceSchemeDto.getImg() != null) {
			try {
				String fileName = StringUtils.cleanPath(insuranceSchemeDto.getImg().getOriginalFilename());
				byte[] bytes = insuranceSchemeDto.getImg().getBytes();
				Path path = Paths.get("./src/main/resources/templates/" + fileName);
		        Files.write(path, bytes);
		        String imagePath = "http://localhost:8083/api/v1/admin/getImage/" +  fileName;
		        insuranceScheme.setImg(imagePath);
		      
			} catch (IllegalStateException | IOException  e) {
				e.printStackTrace();
			}
		}
		
		insuranceSchemeRepository.save(insuranceScheme);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully update insurance schemes");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllCustomers(Integer pageNo) {
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
		
		UserRole userRole = userRoleRepository.findByType(UserRoles.USER.getRole());
		
		Pageable pageable = PageRequest.of(pageNo, 10);
		
		List<User> customer = userRepository.findByRole(userRole, pageable);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully get All Customers.");
		responseDTO.setStatus("success");
		responseDTO.setData(customer);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> changePasswordForAdmin(String password) {
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
		user.setPassword(encoder.encode(password));
		userRepository.save(user);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("Successfully change password.");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

}
