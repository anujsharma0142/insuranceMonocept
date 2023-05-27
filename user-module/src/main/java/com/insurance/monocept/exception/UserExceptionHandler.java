package com.insurance.monocept.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.insurance.monocept.dto.ResponseDto;

@ControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<ResponseDto> handleException(UserNotFoundException e){
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage(e.getMessage());
		responseDTO.setStatus("fail");
		return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseDto> handleException(UserUnAuthorizeException e){
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage(e.getMessage());
		responseDTO.setStatus("fail");
		return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseDto> handleException(UserEmailIdAlreadyExists e){
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage(e.getMessage());
		responseDTO.setStatus("fail");
		return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseDto> handleException(EmailNotValidException e){
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage(e.getMessage());
		responseDTO.setStatus("fail");
		return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseDto> handleException(LoginNotAllowedException e){
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage(e.getMessage());
		responseDTO.setStatus("fail");
		return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseDto> handleException(Exception e){
		ResponseDto responseDTO = new ResponseDto();
		responseDTO.setMessage(e.getMessage());
		responseDTO.setStatus("fail");
		return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
	}

}
