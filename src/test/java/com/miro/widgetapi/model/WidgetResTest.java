package com.miro.widgetapi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.miro.widgetapi.entity.WidgetEntity;

class WidgetResTest {

	@Test
	public void shouldSetAllFieldsWhenCreatingResFromEntity() {
		WidgetEntity entity = entity();
		Widget res = Widget.widgetFromEntity(entity);
		assertEquals(entity.getId(), res.getId());
		assertEquals(entity.getX(), res.getX());
		assertEquals(entity.getY(), res.getY());
		assertEquals(entity.getZ(), res.getZ());
		assertEquals(entity.getHeight(), res.getHeight());
		assertEquals(entity.getWidth(), res.getWidth());
		assertEquals(entity.getLastModification(), res.getLastModification());
		
	}
	
	private WidgetEntity entity() {
		WidgetEntity entity = new WidgetEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setHeight(100);
		entity.setWidth(100);
		entity.setX(0);
		entity.setY(0);
		entity.setZ(0);
		entity.setLastModification(new Date());
		return entity;
	}

}
