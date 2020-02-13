package com.miro.widgetapi.exception;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
	    List<String> errors = new ArrayList<String>();
	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	        errors.add(error.getField() + ": " + error.getDefaultMessage());
	    }
	    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
	        errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
	    }	    
	    
	   return new ResponseEntity<Object>(new ErrorResponse(ex.getMessage(), errors), headers, status);
	}

	class ErrorResponse {
		private String message;
		private List<String> errors;
		private Date timestamp;
		
		public ErrorResponse(String message, List<String> errors) {
			this.message = message;
			this.errors = errors;
			this.timestamp = new Date();
		}

		public String getMessage() {
			return message;
		}
		public List<String> getErrors() {
			return errors;
		}
		public Date getTimestamp() {
			return timestamp;
		}
	}

}
