package com.miro.widgetapi.model;

import java.util.Date;

import com.miro.widgetapi.entity.WidgetEntity;

public class Widget {
	
	private String id;
	private int x;
	private int y;
	private int z;
	private int width;
	private int height;
	private Date lastModification;
	
	public static Widget widgetFromEntity(WidgetEntity entity) {
		Widget res = new Widget();
		res.setId(entity.getId());
		res.setHeight(entity.getHeight());
		res.setWidth(entity.getWidth());
		res.setX(entity.getX());
		res.setY(entity.getY());
		res.setZ(entity.getZ());
		res.setLastModification(entity.getLastModification());
		return res;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getZ() {
		return z;
	}
	public void setZ(Integer z) {
		this.z = z;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Date getLastModification() {
		return lastModification;
	}
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}	

}
