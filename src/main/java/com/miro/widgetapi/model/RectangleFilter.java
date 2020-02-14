package com.miro.widgetapi.model;

import com.miro.widgetapi.exception.InvalidRectangleFilterException;

public class RectangleFilter {

	private Integer minX;
	private Integer minY;
	private Integer maxX;
	private Integer maxY;
	
	public RectangleFilter(Integer minX, Integer minY, Integer maxX, Integer maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public Integer getMinX() {
		return minX;
	}

	public void setMinX(Integer minX) {
		this.minX = minX;
	}

	public Integer getMinY() {
		return minY;
	}

	public void setMinY(Integer minY) {
		this.minY = minY;
	}

	public Integer getMaxX() {
		return maxX;
	}

	public void setMaxX(Integer maxX) {
		this.maxX = maxX;
	}

	public Integer getMaxY() {
		return maxY;
	}

	public void setMaxY(Integer maxY) {
		this.maxY = maxY;
	}
	
	public boolean isValidToUse() throws InvalidRectangleFilterException {
		if(filterValuePresent()) {
			if(missingParameters()) {
				throw new InvalidRectangleFilterException("Missing filter parameters");
			}
            if(minX > maxX) {
            	throw new InvalidRectangleFilterException("minX can not be more than maxX");
            }
            
            if(minY > maxY) {
            	throw new InvalidRectangleFilterException("minY can not be more than maxY");
            }
            
            return true;
		}
		return false;
	}

	private boolean missingParameters() {
		return minX == null || minY == null || maxX == null || maxY == null;
	}

	private boolean filterValuePresent() {
		return minX != null || minY != null || maxX != null || maxY != null;
	}
}
