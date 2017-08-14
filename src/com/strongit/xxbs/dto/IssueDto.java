package com.strongit.xxbs.dto;

import java.util.Date;


public class IssueDto
{
	private String issId;
	private String issNumber;
	private String jourName;
	private Date issTime;
	private String issTime1;
	public String getIssId() {
		return issId;
	}
	public void setIssId(String issId) {
		this.issId = issId;
	}
	public String getIssNumber() {
		return issNumber;
	}
	public void setIssNumber(String issNumber) {
		this.issNumber = issNumber;
	}
	public String getJourName() {
		return jourName;
	}
	public void setJourName(String jourName) {
		this.jourName = jourName;
	}
	public Date getIssTime() {
		return issTime;
	}
	public void setIssTime(Date issTime) {
		this.issTime = issTime;
	}
	public String getIssTime1() {
		return issTime1;
	}
	public void setIssTime1(String issTime1) {
		this.issTime1 = issTime1;
	}
	
}
