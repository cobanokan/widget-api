package com.miro.widgetapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WidgetNotFoundException extends Exception {
	
	private static final long serialVersionUID = 4744940241861983031L;

	public WidgetNotFoundException(String id) {
		super("Widget with id: [" + id + "] does not exist");
	}

}
