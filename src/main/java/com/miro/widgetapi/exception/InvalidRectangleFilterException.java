package com.miro.widgetapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRectangleFilterException extends Exception {

	private static final long serialVersionUID = -1858903190202012370L;

	public InvalidRectangleFilterException(String message) {
		super(message);
	}

}
