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
import org.springframework.stereotype.Service;

import com.insurance.monocept.dto.ResponseDto;
import com.insurance.monocept.dto.SignUpResponseDto;
import com.insurance.monocept.dto.UserSignUpDto;
import com.insurance.monocept.entity.User;
import com.insurance.monocept.entity.UserRole;
import com.insurance.monocept.enums.UserRoles;
import com.insurance.monocept.repository.UserRepository;
import com.insurance.monocept.repository.UserRoleRepository;
import com.insurance.monocept.service.UserService;
import com.insurance.monocept.utility.TokenUtility;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Value("${jwt.app.secret}")
	private String app_secret;
	
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
			user.setEmailVerified(false);
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
			
			ResponseDto responseDTO = new ResponseDto();
			responseDTO.setData(jwtToken);
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
}
