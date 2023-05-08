package com.insurance.monocept.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.insurance.monocept.dto.InsuranceDto;
import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.dto.SignUpResponseDto;
import com.insurance.monocept.dto.UserDetailsDto;
import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.entity.Insurance;
import com.insurance.monocept.entity.InsuranceScheme;
import com.insurance.monocept.entity.InsuranceType;
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
import com.insurance.monocept.service.UserService;
import com.insurance.monocept.utility.AppUtility;
import com.insurance.monocept.utility.TokenUtility;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private InsuranceRepository insuranceRepository;
	
	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;
	
	@Value("${jwt.app.secret}")
	private String app_secret;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private UserUploadDocumentsRepository userUploadDocumentsRepository;
	
	@Override
	public ResponseEntity<?> signUp(UserSignUpDto signUpDto) {
		User user = userRepository.findByEmail(signUpDto.getEmail());

		if (user != null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("Email already exists please Login");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		} else {
			String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(signUpDto.getEmail());
			if (!matcher.matches()) {
				ResponseDto responseDTO = new ResponseDto();
				responseDTO.setMessage("Email is not valid.");
				responseDTO.setStatus("fail");
				return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
			}

			UserRole userRole = userRoleRepository.findByType(UserRoles.USER.getRole());
			if (userRole == null) {
				userRole = new UserRole();
				userRole.setType("ROLE_USER");
				userRoleRepository.save(userRole);
			}
			user = new User();
			user.setEmail(signUpDto.getEmail());
			user.setFirstName(signUpDto.getFirstName());
			user.setLastName(signUpDto.getLastName());
			user.setMobileNo(signUpDto.getMobileNo());
			user.setEmailVerified(true);
			user.setLoginAllowed(true);
			user.setPassword(encoder.encode(signUpDto.getPassword()));
			user.setRole(userRole);
			user.setLastLoginDate(String.valueOf(LocalDate.now()));
			user.setLastLoginTime(String.valueOf(LocalTime.now()));
			userRepository.save(user);

			String jwtToken = TokenUtility.createJWT(user, app_secret, "apiToken", new ArrayList<>());
			SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
			signUpResponseDto.setEmail(signUpDto.getEmail());
			signUpResponseDto.setToken(jwtToken);

			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(signUpResponseDto);
			responseDTO.setMessage("user signup successfully");
			responseDTO.setStatus("success");
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		}

	}

	@Override
	public ResponseEntity<?> login(UserSignUpDto signUpDto) {
		User user = userRepository.findByEmail(signUpDto.getEmail());
		if(user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(null);
			responseDTO.setMessage("No user exists with email : " + signUpDto.getEmail());
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		}
		
//		if (user.getEmailVerified() == false) {
//			ResponseDto responseDTO = new ResponseDto();
//			responseDTO.setData(null);
//			responseDTO.setMessage("User Email is not verify, please verify your account");
//			responseDTO.setStatus("fail");
//			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//		}
		
		if (user.isLoginAllowed() == false) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(null);
			responseDTO.setMessage("Login not allowed");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		user.setLastLoginDate(String.valueOf(LocalDate.now()));
		user.setLastLoginTime(String.valueOf(LocalTime.now()));
		userRepository.save(user);
		if (encoder.matches(signUpDto.getPassword(), user.getPassword())) {
			String jwtToken = TokenUtility.createJWT(user, app_secret, "apiToken", new ArrayList<>());
			Map<String, Object> map = new HashMap<>();
			map.put("jwtToken", jwtToken);
			map.put("role", user.getRole());
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(map);
			responseDTO.setMessage("User login successfully");
			responseDTO.setStatus("success");
			return new ResponseEntity<>(responseDTO, HttpStatus.OK);
		} else {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(null);
			responseDTO.setMessage("incorrect password");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
	}

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
	public ResponseEntity<?> buyInsurance(InsuranceDto insurancedto) {
		
		
		User user = AppUtility.getCurrentUser();

		if (user == null) {
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setMessage("User not found.");
			responseDTO.setStatus("fail");
			return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		}
		
		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(insurancedto.getInsuranceScheme()).orElse(null);
		
		Insurance insurance = new Insurance();
		insurance.setInsuranceScheme(insuranceScheme);
		insurance.setUser(user);
		insurance.setCompleted(false);
		insurance.setStatus("ONGOING");
		insurance.setCompletedSteps("2");
		insurance.setActive(false);
		insuranceRepository.save(insurance);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully insurance");
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
		Insurance insurance = insuranceRepository.findById(insuranceId).orElse(null);
		UserUploadDocuments documents = new UserUploadDocuments();
				try {
					String fileName = StringUtils.cleanPath(panCard.getOriginalFilename());
					byte[] bytes = panCard.getBytes();
					Path path = Paths.get("./src/main/resources/templates/" + fileName);
			        Files.write(path, bytes);
			        String imagePath = "http://localhost:8083/api/vi/admin/getImage/" +  fileName;
			        documents.setPanCard(imagePath);			      
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				}
				try {
					String fileName = StringUtils.cleanPath(adhaarFront.getOriginalFilename());
					byte[] bytes = adhaarFront.getBytes();
					Path path = Paths.get("./src/main/resources/templates/" + fileName);
			        Files.write(path, bytes);
			        String imagePath = "http://localhost:8083/api/vi/admin/getImage/" +  fileName;
			        documents.setPanCard(imagePath);			      
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				}
				try {
					String fileName = StringUtils.cleanPath(adhaarBack.getOriginalFilename());
					byte[] bytes = adhaarBack.getBytes();
					Path path = Paths.get("./src/main/resources/templates/" + fileName);
			        Files.write(path, bytes);
			        String imagePath = "http://localhost:8083/api/vi/admin/getImage/" +  fileName;
			        documents.setPanCard(imagePath);			      
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				}
		documents.setInsurance(insurance);
		documents.setUser(user);
		documents.setStatus("PENDING");
		userUploadDocumentsRepository.save(documents);	
		insurance.setCompletedSteps("3");
		insuranceRepository.save(insurance);
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
		UserDetails userDetails = new UserDetails();
		userDetails.setAddress(userDetailsDto.getAddress());
		userDetails.setCity(userDetailsDto.getCity());
		userDetails.setPincode(userDetailsDto.getPincode());
		userDetails.setState(userDetailsDto.getState());
		userDetails.setPanCard(userDetailsDto.getPanCard());
		userDetailsRepository.save(userDetails);
		
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage("successfully add user Details");
		responseDTO.setStatus("success");
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}
	
	
	
	
}
