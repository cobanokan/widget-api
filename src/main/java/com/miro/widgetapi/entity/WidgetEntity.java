package com.miro.widgetapi.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "widget")
public class WidgetEntity implements Comparable<WidgetEntity> {

	@Id
	private String id;
	private Integer x;
	private Integer y;
	private Integer z;
	private Integer width;
	private Integer height;
	private Date lastModification;

	public WidgetEntity() {
	}
	
	public WidgetEntity(WidgetEntity entity) {
		this.id = entity.getId();
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
		this.height = entity.getHeight();
		this.width = entity.getWidth();
		this.lastModification = entity.getLastModification();
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

	@Override
	public int compareTo(WidgetEntity widget) {
		return this.getZ() - widget.getZ();
	}
}
