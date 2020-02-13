package com.miro.widgetapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WidgetPageSizeTooBigException extends Exception {
	
	private static final long serialVersionUID = 4744940241861983031L;

	public WidgetPageSizeTooBigException(Integer size) {
		super("Requested page size can not be more than 500. requested [" + size + "]");
	}

}
