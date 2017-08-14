package com.strongit.oa.wap.util;

public class Page {

	private Integer totalCount;
	
	private Integer totalPages;
	
	public Page(Integer totalCount,Integer totalPages) {
		this.totalCount = totalCount;
		this.totalPages = totalPages;
	}
	
	public Page() {
		
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
