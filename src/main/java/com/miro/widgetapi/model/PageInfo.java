package com.miro.widgetapi.model;

public class PageInfo {
	private long totalElements;
	private int totalPages;
	private int size;
	private int number;
	
	public PageInfo() {
	}
	
	public PageInfo(long totalElements, int totalPages, int size, int number) {
		this.setTotalElements(totalElements);
		this.setTotalPages(totalPages);
		this.setSize(size);
		this.setNumber(number);
	}
	
	public long getTotalElements() {
		return totalElements;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getSize() {
		return size;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
