package com.strongit.oa.wap.util;

public class Status {

	private String code;

	private String failReason;
	
	public Status() {
		
	}
	
	public Status(String code,String failReason) {
		this.code = code;
		this.failReason = failReason;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

}
