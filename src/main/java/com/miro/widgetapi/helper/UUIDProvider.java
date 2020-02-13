package com.miro.widgetapi.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDProvider {
	
	public String random() {
		return UUID.randomUUID().toString();
	}
}
