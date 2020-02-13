package com.miro.widgetapi.model;

public class PageInfo {
	private long totalElements;
	private int totalPages;
	private int size;
	private int number;
	
	public PageInfo(long totalElements, int totalPages, int size, int number) {
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.size = size;
		this.number = number;
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
}
