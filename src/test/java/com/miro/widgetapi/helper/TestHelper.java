package com.miro.widgetapi.helper;

import java.util.Date;
import java.util.UUID;

import com.miro.widgetapi.entity.WidgetEntity;
import com.miro.widgetapi.model.WidgetPatchSource;
import com.miro.widgetapi.model.WidgetSource;

public class TestHelper {

	public static WidgetEntity simpleEntityWithZ(Integer z) {
		WidgetEntity entity = new WidgetEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setHeight(100);
		entity.setWidth(100);
		entity.setX(0);
		entity.setY(0);
		entity.setZ(z);
		entity.setLastModification(new Date());
		return entity;
	}
	
	public static WidgetSource simpleSource(Integer z) {
		WidgetSource source = new WidgetSource();
		source.setHeight(100);
		source.setWidth(100);
		source.setX(0);
		source.setY(0);
		source.setZ(z);
		return source;
	}
	
	public static WidgetSource source(Integer x, Integer y, Integer z, Integer width, Integer heigth) {
		WidgetSource source = new WidgetSource();
		source.setHeight(heigth);
		source.setWidth(width);
		source.setX(x);
		source.setY(y);
		source.setZ(z);
		return source;
	}
	
	public static WidgetPatchSource simplePatchSource(Integer z) {
		WidgetPatchSource source = new WidgetPatchSource();
		source.setHeight(100);
		source.setWidth(100);
		source.setX(0);
		source.setY(0);
		source.setZ(z);
		return source;
	}
}
