package com.strongit.xxbs.dto;

import java.util.Date;


public class PublishDto
{
	private String pubId;
	
	private String pubTitle;
	
	private String pubSubmitDate;
	
	private String pubDate;
	
	private String orgName;
	
	private String orgId;
	
	private String pubMergeOrg;
	
	private String issId;
	
	private String issNumber;
	
	private String jourName;

	public String getPubId()
	{
		return pubId;
	}

	public void setPubId(String pubId)
	{
		this.pubId = pubId;
	}

	public String getPubTitle()
	{
		return pubTitle;
	}

	public void setPubTitle(String pubTitle)
	{
		this.pubTitle = pubTitle;
	}


	public String getPubSubmitDate() {
		return pubSubmitDate;
	}

	public void setPubSubmitDate(String pubSubmitDate) {
		this.pubSubmitDate = pubSubmitDate;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getPubMergeOrg() {
		return pubMergeOrg;
	}

	public void setPubMergeOrg(String pubMergeOrg) {
		this.pubMergeOrg = pubMergeOrg;
	}

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
	
	
}
