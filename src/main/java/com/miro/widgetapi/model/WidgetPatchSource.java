package com.miro.widgetapi.model;

import java.util.Optional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class WidgetPatchSource {
	
	private Optional<@NotNull Integer> x;
	private Optional<@NotNull Integer> y;
	private Optional<@NotNull Integer> z;
	private Optional<@NotNull @Min(1) Integer> width;
	private Optional<@NotNull @Min(1) Integer> height;
	
	public Optional<Integer> getX() {
		return x == null ? Optional.empty() : x;
	}
	public void setX(Integer x) {
		this.x = Optional.ofNullable(x);
	}
	public Optional<Integer> getY() {
		return y == null ? Optional.empty() : y;
	}
	public void setY(Integer y) {
		this.y = Optional.ofNullable(y);
	}
	public Optional<Integer> getZ() {
		return z == null ? Optional.empty() : z;
	}
	public void setZ(Integer z) {
		this.z = Optional.ofNullable(z);
	}
	public Optional<Integer> getWidth() {
		return width == null ? Optional.empty() : width;
	}
	public void setWidth(Integer width) {
		this.width = Optional.ofNullable(width);
	}
	public Optional<Integer> getHeight() {
		return height == null ? Optional.empty() : height;
	}
	public void setHeight(@NotNull Integer height) {
		this.height = Optional.of(height);
	}
}
